package org.egorsemenovv.productsservice.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import org.egorsemenovv.productsservice.model.Color;
import org.egorsemenovv.productsservice.validator.ValidEnum;

@Getter
public class SkuCreateEditDto {

    @NotNull
    @Min(1)
    private Long productId;

    @Size(min = 1, max = 64)
    private String code;

    @ValidEnum(enumClass = Color.class)
    @NotNull
    @Size(min = 1, max = 32)
    private String color;

    @NotNull
    @Min(value = 1, message = "stock should be greater or equals 1")
    private Integer stock;
}
