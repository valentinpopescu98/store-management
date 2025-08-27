package com.valentinpopescu.store.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductCreateRequest (

        @NotBlank
        String productCode,

        @NotBlank
        String name,

        @NotNull
        @Positive
        BigDecimal price
) {

}
