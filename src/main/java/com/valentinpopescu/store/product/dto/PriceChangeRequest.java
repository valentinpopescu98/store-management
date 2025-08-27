package com.valentinpopescu.store.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PriceChangeRequest (

        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal price
) {

}
