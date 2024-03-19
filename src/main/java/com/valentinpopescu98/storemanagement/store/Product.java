package com.valentinpopescu98.storemanagement.store;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Product {

    @Id
    @SequenceGenerator(allocationSize = 1, name = "product_sequence", sequenceName = "product_sequence")
    @GeneratedValue(generator = "product_sequence", strategy = IDENTITY)
    private final Long id;
    @NotNull(message = "Product must have a name")
    @NotEmpty(message = "Product name must not be blank")
    private String name;
    @NotNull(message = "Product must have a description")
    @NotEmpty(message = "Product description must not be blank")
    private String description;
    // price in cents
    @NotNull(message = "Product must have a price")
    private Long price;
    // manufacturer's suggested retail price in cents
    private Long msrp;
    @NotNull(message = "Product must have a stock")
    private Long stock;

    // no args constructor to enable JPA for hydrating entities
    public Product() {
        id = null;
    }

    public Product(Long id, String name, String description, Long price, Long msrp, Long stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.msrp = msrp;
        this.stock = stock;
    }

    public Product(String name, String description, Long price, Long msrp, Long stock) {
        id = null;
        this.name = name;
        this.description = description;
        this.price = price;
        this.msrp = msrp;
        this.stock = stock;
    }

}
