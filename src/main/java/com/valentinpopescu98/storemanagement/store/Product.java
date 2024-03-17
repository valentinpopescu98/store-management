package com.valentinpopescu98.storemanagement.store;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @SequenceGenerator(allocationSize = 1, name = "product_sequence", sequenceName = "product_sequence")
    @GeneratedValue(generator = "product_sequence", strategy = IDENTITY)
    private final Long id;
    private String name;
    private String description;
    // price in cents
    private Long price;
    // manufacturer's suggested retail price in cents
    private Long msrp;
    private Long stock;

    // no args constructor to enable JPA for hydrating entities
    public Product() {
        id = null;
    }

}
