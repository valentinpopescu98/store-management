package com.valentinpopescu.store.product.controller;

import com.valentinpopescu.store.product.dto.PriceChangeRequest;
import com.valentinpopescu.store.product.dto.ProductCreateRequest;
import com.valentinpopescu.store.product.dto.ProductResponse;
import com.valentinpopescu.store.product.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse add(@RequestBody @Valid ProductCreateRequest request) {
        return service.add(request);
    }

    @GetMapping("/{productCode}")
    public ProductResponse find(@PathVariable @NotBlank String productCode) {
        return service.findByProductCode(productCode);
    }

    @GetMapping
    public List<ProductResponse> findAll() {
        return service.findAll();
    }

    @PatchMapping("/{productCode}/price")
    public ProductResponse changePrice(
            @PathVariable @NotBlank String productCode,
            @RequestBody @Valid PriceChangeRequest request) {
        return service.changePrice(productCode, request);
    }

    @DeleteMapping("/{productCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @NotBlank String productCode) {
        service.deleteByProductCode(productCode);
    }
}
