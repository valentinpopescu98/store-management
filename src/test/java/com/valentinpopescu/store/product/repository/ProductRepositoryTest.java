package com.valentinpopescu.store.product.repository;

import com.valentinpopescu.store.product.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test
    void findByProductCodeShouldReturnWhenProductCodeExists() {
        Product saved = repository.save(product("code"));

        Optional<Product> product = repository.findByProductCode("code");

        assertTrue(product.isPresent());
        assertEquals(saved.getId(), product.get().getId());
        assertEquals("code", product.get().getProductCode());
        assertEquals(new BigDecimal("10.00"), product.get().getPrice());
    }

    @Test
    void findByProductCodeShouldNotReturnWHenProductCodeMissing() {
        assertTrue(repository.findByProductCode("missing").isEmpty());
    }

    @Test
    void existsByProductCodeShouldReturnTrueAndFalse() {
        repository.save(product("exist"));

        assertTrue(repository.existsByProductCode("exist"));
        assertFalse(repository.existsByProductCode("missing"));
    }

    @Test
    void saveShouldThrowDataIntegrityViolationWhenProductCodeExists() {
        repository.save(product("exist"));

        Product product = product("exist");
        assertThrows(DataIntegrityViolationException.class, () -> repository.saveAndFlush(product));
    }

    private Product product(String productCode) {
        return new Product(
                productCode,
                "name",
                new BigDecimal("10.00")
        );
    }
}