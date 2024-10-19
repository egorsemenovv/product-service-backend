package org.egorsemenovv.productsservice.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import org.egorsemenovv.productsservice.model.Color;
import org.egorsemenovv.productsservice.validator.ValidEnum;

@Getter
public class SkuCreateEditDto {

    @ValidEnum(enumClass = Color.class)
    @NotNull
    @Size(min = 1, max = 32)
    private String color;

    @NotNull
    @Min(value = 1, message = "stock should be greater or equals 1")
    private Integer stock;
}
