package org.egorsemenovv.productsservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SkuDocument {
    private String code;
    private String color;
    private Integer stock;
}
