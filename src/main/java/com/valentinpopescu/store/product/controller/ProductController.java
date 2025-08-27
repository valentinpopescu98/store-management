package com.valentinpopescu.store.product.controller;

import com.valentinpopescu.store.product.model.Product;
import com.valentinpopescu.store.product.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product add(@RequestBody @Valid Product product) {
        return service.add(product);
    }

    @GetMapping("/{productCode}")
    public Product find(@PathVariable @NotBlank String productCode) {
        return service.findByProductCode(productCode);
    }

    @GetMapping
    public List<Product> findAll() {
        return service.findAll();
    }

    @PatchMapping("/{productCode}/price")
    public Product changePrice(
            @PathVariable @NotBlank String productCode,
            @RequestParam @DecimalMin(value = "0.01") BigDecimal price) {
        return service.changePrice(productCode, price);
    }

    @DeleteMapping("/{productCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @NotBlank String productCode) {
        service.deleteByProductCode(productCode);
    }
}
