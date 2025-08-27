package com.valentinpopescu.store.product.service;

import com.valentinpopescu.store.product.dto.PriceChangeRequest;
import com.valentinpopescu.store.product.dto.ProductCreateRequest;
import com.valentinpopescu.store.product.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse add(ProductCreateRequest request);
    ProductResponse findByProductCode(String productCode);
    List<ProductResponse> findAll();
    ProductResponse changePrice(String productCode, PriceChangeRequest request);
    void deleteByProductCode(String productCode);
}
