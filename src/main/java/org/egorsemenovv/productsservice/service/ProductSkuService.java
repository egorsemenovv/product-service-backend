package org.egorsemenovv.productsservice.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egorsemenovv.productsservice.dto.ProductCreateEditDto;
import org.egorsemenovv.productsservice.dto.ProductSkuDocument;
import org.egorsemenovv.productsservice.dto.SkuCreateEditDto;
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

    public int loadProductsSkuToElastic(Boolean active, LocalDate startDate) {
        int numberOfLoadedSkus = 0;
        int pageNumber = 0;
        int pageSize = 20;
        Page<Sku> skuPage;

        do {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            skuPage = skuRepository.findUnloadedSkuByFilter(active, startDate, pageable);

            List<Sku> skus = skuPage.getContent();
            numberOfLoadedSkus += skus.size();

            List<ProductSkuDocument> productSkuDocuments = skus.stream()
                    .map(this::createProductSkuDocument).toList();

            try {
                loadToElastic(productSkuDocuments);
            } catch (IOException e) {
                log.error("failed load data to elastic for products {}", productSkuDocuments);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            skuRepository.updateLoadedStatusForSku(skus.stream().map(Sku::getId).toList(), true);

        } while (skuPage.hasNext());

        return numberOfLoadedSkus;
    }

    private void loadToElastic(List<ProductSkuDocument> documents) throws IOException {
        if (documents.isEmpty()) {
            return;
        }
        BulkRequest.Builder bulkRequestBuilder = new BulkRequest.Builder();
        for (ProductSkuDocument document : documents) {
            bulkRequestBuilder.operations(op -> op.index(idx -> idx
                    .index("products_skus_index")
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

    private ProductSkuDocument createProductSkuDocument(Sku sku) {
        return ProductSkuDocument.builder()
                .id(sku.getProduct().getId())
                .name(sku.getProduct().getName())
                .description(sku.getProduct().getDescription())
                .price(sku.getProduct().getPrice().doubleValue())
                .active(sku.getProduct().getActive())
                .startDate(sku.getProduct().getStartDate())
                .code(sku.getCode())
                .color(sku.getColor().name())
                .stock(sku.getStock())
                .build();
    }

}
