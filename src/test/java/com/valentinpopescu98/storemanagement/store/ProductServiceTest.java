package com.valentinpopescu98.storemanagement.store;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    public static final Product DUMMY_PRODUCT_NULL_ID =
            new Product("dummy-name", "dummy-description", 1L, null, 0L);
    public static final Product DUMMY_PRODUCT =
            new Product(1L, "dummy-name", "dummy-description", 1L, null, 0L);
    private static final Long DUMMY_ID = 1L;

    @Mock
    private ProductRepository repository;
    private ProductService service;

    @BeforeEach
    void setUp() {
        // given
        service = new ProductService(repository);
    }

    @Test
    void getOneTest() {
        // given
        given(repository.findById(anyLong()))
                .willReturn(Optional.of(DUMMY_PRODUCT));

        // when
        Product product = service.getOne(DUMMY_ID);

        // then
        assertEquals(DUMMY_PRODUCT, product);
    }

    @Test
    void getOneInvalidIdTest() {
        // given
        given(repository.findById(anyLong()))
                .willReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> service.getOne(DUMMY_ID));
    }

    @Test
    void getAllTest() {
        // given
        List<Product> expectedList = List.of(DUMMY_PRODUCT, DUMMY_PRODUCT);
        given(repository.findAll())
                .willReturn(expectedList);

        // when
        List<Product> products = service.getAll();

        // then
        assertEquals(expectedList, products);
    }

    @Test
    void getAllNoProductFoundTest() {
        // given
        given(repository.findAll())
                .willReturn(new ArrayList<>());

        // then
        assertThrows(EmptyResultDataAccessException.class, () -> service.getAll());
    }

    @Test
    void addTest() {
        // given
        given(repository.existsByName(anyString()))
                .willReturn(false);
        given(repository.save(any(Product.class)))
                .willReturn(DUMMY_PRODUCT);

        // when
        Product product = service.add(DUMMY_PRODUCT_NULL_ID);

        // then
        assertEquals(DUMMY_PRODUCT, product);
        verify(repository, times(1)).save(DUMMY_PRODUCT_NULL_ID);
    }

    @Test
    void addNameAlreadyExistingTest() {
        // given
        given(repository.existsByName(anyString()))
                .willReturn(true);

        // then
        assertThrows(DataIntegrityViolationException.class, () -> service.add(DUMMY_PRODUCT_NULL_ID));
    }

    @Test
    void addMissingFieldsProductTest() {
        // given
        Product nullNameProduct =
                new Product(null, "dummy-description", 1L, null, 0L);
        Product nullDescriptionProduct =
                new Product("dummy-name", null, 1L, null, 0L);
        Product nullPriceProduct =
                new Product("dummy-name", "dummy-description", null, null, 0L);
        Product nullStockProduct =
                new Product("dummy-name", "dummy-description", 1L, null, null);

        // then
        assertThrows(IllegalArgumentException.class, () -> service.add(nullNameProduct));
        assertThrows(IllegalArgumentException.class, () -> service.add(nullDescriptionProduct));
        assertThrows(IllegalArgumentException.class, () -> service.add(nullPriceProduct));
        assertThrows(IllegalArgumentException.class, () -> service.add(nullStockProduct));
    }

    @Test
    void addZeroPriceProduct() {
        // given
        Product product =
                new Product("dummy-name", "dummy-description", 0L, null, 0L);

        // then
        assertThrows(IllegalArgumentException.class, () -> service.add(product));
    }

    @Test
    void updateTest() {
        // given
        given(repository.findById(anyLong()))
                .willReturn(Optional.of(DUMMY_PRODUCT));
        given(repository.existsByName(anyString()))
                .willReturn(false);
        given(repository.save(any(Product.class)))
                .willReturn(DUMMY_PRODUCT);

        // when
        Product product = service.update(DUMMY_ID, DUMMY_PRODUCT_NULL_ID);

        // then
        assertEquals(DUMMY_PRODUCT, product);
        verify(repository, times(1)).save(DUMMY_PRODUCT);
    }

    @Test
    void updateInvalidIdTest() {
        // given
        given(repository.findById(anyLong()))
                .willReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> service.update(DUMMY_ID, DUMMY_PRODUCT_NULL_ID));
    }

    @Test
    void updateNameAlreadyExistingTest() {
        // given
        given(repository.findById(anyLong()))
                .willReturn(Optional.of(DUMMY_PRODUCT));
        given(repository.existsByName(anyString()))
                .willReturn(true);

        // then
        assertThrows(DataIntegrityViolationException.class, () -> service.update(DUMMY_ID, DUMMY_PRODUCT_NULL_ID));
    }

    @Test
    void updateZeroPriceProduct() {
        // given
        Product product =
                new Product("dummy-name", "dummy-description", 0L, null, 0L);
        given(repository.findById(anyLong()))
                .willReturn(Optional.of(DUMMY_PRODUCT));
        given(repository.existsByName(anyString()))
                .willReturn(false);

        // then
        assertThrows(IllegalArgumentException.class, () -> service.update(DUMMY_ID, product));
    }

    @Test
    void removeTest() {
        // given
        given(repository.findById(anyLong()))
                .willReturn(Optional.of(DUMMY_PRODUCT));

        // when
        service.remove(DUMMY_ID);

        // then
        verify(repository, times(1)).delete(DUMMY_PRODUCT);
    }

    @Test
    void removeInvalidIdTest() {
        // given
        given(repository.findById(anyLong()))
                .willReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> service.remove(DUMMY_ID));
    }

}