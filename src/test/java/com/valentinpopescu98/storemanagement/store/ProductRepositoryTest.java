package com.valentinpopescu98.storemanagement.store;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ProductRepositoryTest {

    public static final Product DUMMY_PRODUCT_NULL_ID =
            new Product("dummy-name", "dummy-description", 1L, null, 0L);
    public static final Product DUMMY_PRODUCT =
            new Product(1L, "dummy-name", "dummy-description", 1L, null, 0L);

    private static final String DUMMY_NAME = "dummy-name";

    @Autowired
    private ProductRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void existsByNameTest() {
        // given
        repository.save(DUMMY_PRODUCT_NULL_ID);

        // when
        boolean doesExist = repository.existsByName(DUMMY_NAME);

        // then
        assertTrue(doesExist);
    }

    @Test
    void existsByNameNotFoundTest() {
        // when
        boolean doesExist = repository.existsByName(DUMMY_NAME);

        // then
        assertFalse(doesExist);
    }

}