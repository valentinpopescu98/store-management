package com.valentinpopescu98.storemanagement.store;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Product {

    @Id
    @SequenceGenerator(allocationSize = 1, name = "product_seq", sequenceName = "product_seq")
    @GeneratedValue(generator = "product_seq", strategy = IDENTITY)
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

    public Product(String name, String description, Long price, Long msrp, Long stock) {
        id = null;
        this.name = name;
        this.description = description;
        this.price = price;
        this.msrp = msrp;
        this.stock = stock;
    }

}
