package org.egorsemenovv.productsservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class ProductCreateEditDto {

    @NotNull
    @Length(max = 64)
    private String productName;

    private String description;

    @DecimalMin(value = "0.01")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

    @NotNull
    private Boolean active;

    @NotNull
    private LocalDate startDate;
}
