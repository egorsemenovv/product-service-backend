package org.egorsemenovv.productsservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.egorsemenovv.productsservice.dto.ProductCreateEditDto;
import org.egorsemenovv.productsservice.dto.SkuCreateEditDto;
import org.egorsemenovv.productsservice.mapper.ProductCreateEditMapper;
import org.egorsemenovv.productsservice.mapper.SkuCreateEditMapper;
import org.egorsemenovv.productsservice.model.Product;
import org.egorsemenovv.productsservice.model.Sku;
import org.egorsemenovv.productsservice.repository.ProductRepository;
import org.egorsemenovv.productsservice.repository.SkuRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductSkuService {

    private final SkuRepository skuRepository;
    private final ProductRepository productRepository;
    private final ProductCreateEditMapper productCreateEditMapper;
    private final SkuCreateEditMapper skuCreateEditMapper;

    public Long createProduct(ProductCreateEditDto productCreateEditDto){
        Product product = productCreateEditMapper.map(productCreateEditDto);
        productRepository.saveAndFlush(product);
        return product.getId();
    }

    public void deleteProductById(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        productRepository.delete(product);
    }

    public String createSku(SkuCreateEditDto skuCreateEditDto, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        Sku sku = skuCreateEditMapper.map(skuCreateEditDto);
        sku.setProduct(product);
        skuRepository.saveAndFlush(sku);
        return sku.getCode();
    }


}
