package org.egorsemenovv.productsservice.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egorsemenovv.productsservice.dto.ProductCreateEditDto;
import org.egorsemenovv.productsservice.dto.ProductDocument;
import org.egorsemenovv.productsservice.dto.SkuCreateEditDto;
import org.egorsemenovv.productsservice.dto.SkuDocument;
import org.egorsemenovv.productsservice.mapper.ProductCreateEditMapper;
import org.egorsemenovv.productsservice.mapper.SkuCreateEditMapper;
import org.egorsemenovv.productsservice.model.Product;
import org.egorsemenovv.productsservice.model.Sku;
import org.egorsemenovv.productsservice.repository.ProductRepository;
import org.egorsemenovv.productsservice.repository.SkuRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductSkuService {

    private final SkuRepository skuRepository;
    private final ProductRepository productRepository;
    private final ProductCreateEditMapper productCreateEditMapper;
    private final SkuCreateEditMapper skuCreateEditMapper;
    private final ElasticsearchClient esClient;

    public Long createProduct(ProductCreateEditDto productCreateEditDto) {
        Product product = productCreateEditMapper.map(productCreateEditDto);
        productRepository.saveAndFlush(product);
        return product.getId();
    }

    public String createSku(SkuCreateEditDto skuCreateEditDto, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        Sku sku = skuCreateEditMapper.map(skuCreateEditDto);
        sku.setProduct(product);
        skuRepository.saveAndFlush(sku);
        return sku.getCode();
    }

    public int loadProductsSkuToElastic() {
        int numberOfLoadedProducts = 0;
        int page = 0;
        int size = 20;
        Page<Product> productPage;
        do {
            Pageable pageable = PageRequest.of(page, size);
            productPage = productRepository.findUnloadedProductByFilter(true, LocalDate.now(), pageable);

            List<Product> products = productPage.getContent();
            numberOfLoadedProducts += products.size();

            List<ProductDocument> productDocuments = products.stream()
                    .map(this::createProductDocument).toList();

            try {
                loadToElastic(productDocuments);
            } catch (IOException e) {
                log.error("failed load data to elastic for products {}", productDocuments);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            productRepository.updateLoadedStatusForProduct(productPage.stream().map(Product::getId).toList(), true);

            page++;

        } while (productPage.hasNext());

        return numberOfLoadedProducts;
    }

    private void loadToElastic(List<ProductDocument> documents) throws IOException {
        if (documents.isEmpty()) {
            return;
        }
        BulkRequest.Builder bulkRequestBuilder = new BulkRequest.Builder();
        for (ProductDocument document : documents) {
            bulkRequestBuilder.operations(op -> op.index(idx -> idx
                    .index("products_skus_index")
                    .id(String.valueOf(idx))
                    .document(document)
            ));
        }

        BulkResponse result = esClient.bulk(bulkRequestBuilder.build());

        if (result.errors()) {
            log.error("Bulk had errors");
            for (BulkResponseItem item : result.items()) {
                if (item.error() != null) {
                    log.error(item.error().reason());
                }
            }
        }
    }

    private ProductDocument createProductDocument(Product product) {
        List<SkuDocument> skus = product.getSkus().stream().map(this::createSkuDocument).toList();
        return ProductDocument.builder()
                .productId(product.getId())
                .productName(product.getName())
                .description(product.getDescription())
                .price(product.getPrice().doubleValue())
                .isActive(product.getActive())
                .startDate(product.getStartDate())
                .skus(skus)
                .build();
    }

    private SkuDocument createSkuDocument(Sku sku) {
        return SkuDocument.builder()
                .code(sku.getCode())
                .color(sku.getColor().name())
                .stock(sku.getStock())
                .build();
    }
}
