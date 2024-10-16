package org.egorsemenovv.productsservice.mapper;

import org.egorsemenovv.productsservice.dto.ProductCreateEditDto;
import org.egorsemenovv.productsservice.model.Product;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ProductCreateEditMapper implements Mapper<ProductCreateEditDto, Product>{

    @Override
    public Product map(ProductCreateEditDto productCreateEditDto) {
        return Product.builder()
                .name(productCreateEditDto.getProductName())
                .description(productCreateEditDto.getDescription())
                .price(productCreateEditDto.getPrice())
                .active(productCreateEditDto.getActive())
                .startDate(productCreateEditDto.getStartDate())
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();
    }
}
