package com.valentinpopescu.store.product.dto;

import java.math.BigDecimal;

public record ProductResponse(

        Long id,
        String productCode,
        String name,
        BigDecimal price
) {

}
