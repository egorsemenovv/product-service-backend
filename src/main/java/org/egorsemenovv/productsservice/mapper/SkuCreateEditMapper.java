package org.egorsemenovv.productsservice.mapper;

import org.egorsemenovv.productsservice.dto.SkuCreateEditDto;
import org.egorsemenovv.productsservice.model.Color;
import org.egorsemenovv.productsservice.model.Sku;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;


@Component
public class SkuCreateEditMapper implements Mapper<SkuCreateEditDto, Sku>{

    @Override
    public Sku map(SkuCreateEditDto skuCreateEditDto) {
        return Sku.builder()
                .color(Color.valueOf(skuCreateEditDto.getColor().toUpperCase()))
                .stock(skuCreateEditDto.getStock())
                .code(UUID.randomUUID().toString())
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
            .build();
    }
}
