package com.valentinpopescu.store.product.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String productCode;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    public Product(String productCode, String name, BigDecimal price) {
        this.productCode = productCode;
        this.name = name;
        this.price = price;
    }
}
