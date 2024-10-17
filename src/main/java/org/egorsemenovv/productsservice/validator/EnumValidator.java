package org.egorsemenovv.productsservice.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Class<? extends  Enum<?>> enumClass;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true; // should be used with @NotNull
        }
        final String finalValue = value.toUpperCase();
        return Arrays.stream(enumClass.getEnumConstants())
                .anyMatch(e -> e.name().equals(finalValue));
    }
}
