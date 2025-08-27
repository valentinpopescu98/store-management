package com.valentinpopescu.store.product.service;

import com.valentinpopescu.store.exceptions.BadRequestException;
import com.valentinpopescu.store.exceptions.NotFoundException;
import com.valentinpopescu.store.product.dto.PriceChangeRequest;
import com.valentinpopescu.store.product.dto.ProductCreateRequest;
import com.valentinpopescu.store.product.dto.ProductResponse;
import com.valentinpopescu.store.product.model.Product;
import com.valentinpopescu.store.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository repository;
    private ProductServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ProductServiceImpl(repository);
    }

    @Test
    void addShouldThrowBadRequestWhenProductCodeExists() {
        when(repository.existsByProductCode("code"))
                .thenReturn(true);

        var request = productCreateReq("code", "name", "10.00");
        assertThrows(BadRequestException.class, () -> service.add(request));

        verify(repository, never()).save(any());
    }

    @Test
    void addShouldSaveAndReturnResponse() {
        when(repository.existsByProductCode("code"))
                .thenReturn(false);
        when(repository.save(any(Product.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        var request = productCreateReq("code", "name", "10.00");
        ProductResponse resp = service.add(request);

        assertEquals("code", resp.productCode());
        assertEquals("name", resp.name());
        assertEquals(new BigDecimal("10.00"), resp.price());
        verify(repository).save(any(Product.class));
    }

    @Test
    void findByProductCodeShouldReturnWhenExists() {
        Product product = product("code", "name", "10.00");
        when(repository.findByProductCode("code"))
                .thenReturn(Optional.of(product));

        ProductResponse response = service.findByProductCode("code");

        assertEquals("code", response.productCode());
        assertEquals("name", response.name());
        assertEquals(new BigDecimal("10.00"), response.price());
    }

    @Test
    void findByProductCodeShouldThrowNotFoundWhenMissing() {
        when(repository.findByProductCode("missing"))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.findByProductCode("missing"));
    }

    @Test
    void findAllShouldMapAllProductsToProductResponses() {
        Product p1 = product("abc", "first", "1.00");
        Product p2 = product("def", "second", "2.00");
        when(repository.findAll())
                .thenReturn(List.of(p1, p2));

        List<ProductResponse> list = service.findAll();

        assertEquals(2, list.size());
        assertEquals("abc", list.get(0).productCode());
        assertEquals("def", list.get(1).productCode());
        assertEquals("first", list.get(0).name());
        assertEquals("second", list.get(1).name());
        assertEquals(new BigDecimal("1.00"), list.get(0).price());
        assertEquals(new BigDecimal("2.00"), list.get(1).price());
    }

    @Test
    void changePriceShouldUpdateAndReturnResponse() {
        Product existing = product("code", "name", "10.00");
        when(repository.findByProductCode("code"))
                .thenReturn(Optional.of(existing));

        ProductResponse response = service.changePrice("code",
                new PriceChangeRequest(new BigDecimal("25.55")));

        assertEquals(new BigDecimal("25.55"), response.price());
        assertEquals(new BigDecimal("25.55"), existing.getPrice());
        verify(repository, never()).save(any());
    }

    @Test
    void changePriceShouldThrowNotFoundWhenMissing() {
        when(repository.findByProductCode("missing"))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.changePrice("missing",
                new PriceChangeRequest(new BigDecimal("5.00"))));
    }

    @Test
    void deleteByProductCodeShouldDeleteWhenExists() {
        var existing = product("code", "name", "10.00");
        when(repository.findByProductCode("code"))
                .thenReturn(Optional.of(existing));

        service.deleteByProductCode("code");

        verify(repository).delete(existing);
    }

    @Test
    void deleteByProductCodeShouldThrowNotFoundWhenMissing() {
        when(repository.findByProductCode("missing"))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.deleteByProductCode("missing"));
        verify(repository, never()).delete(any());
    }

    private Product product(String productCode, String name, String price) {
        return new Product(
                productCode,
                name,
                new BigDecimal(price)
        );
    }

    private ProductCreateRequest productCreateReq(String productCode, String name, String price) {
        return new ProductCreateRequest(
                productCode,
                name,
                new BigDecimal(price)
        );
    }
}