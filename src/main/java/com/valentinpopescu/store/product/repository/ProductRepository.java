package com.valentinpopescu.store.product.repository;

import com.valentinpopescu.store.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductCode(String productCode);
    boolean existsByProductCode(String productCode);
}
