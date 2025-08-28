package com.valentinpopescu.store.product.common;

import com.valentinpopescu.store.product.dto.ProductCreateRequest;
import com.valentinpopescu.store.product.dto.ProductResponse;
import com.valentinpopescu.store.product.model.Product;

public class ProductSamples {

    private ProductSamples() {
    }

    public static ProductResponse productToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getProductCode(),
                product.getName(),
                product.getPrice()
        );
    }

    public static Product requestToProduct(ProductCreateRequest request) {
        return new Product(
                request.productCode(),
                request.name(),
                request.price()
        );
    }
}
