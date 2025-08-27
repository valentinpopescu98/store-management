package com.valentinpopescu.store.product.service;

import com.valentinpopescu.store.product.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    Product add(Product product);
    Product findByProductCode(String productCode);
    List<Product> findAll();
    Product changePrice(String productCode, BigDecimal price);
    void deleteByProductCode(String productCode);
}
