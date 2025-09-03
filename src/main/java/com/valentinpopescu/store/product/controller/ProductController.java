package com.valentinpopescu.store.product.controller;

import com.valentinpopescu.store.product.dto.PriceChangeRequest;
import com.valentinpopescu.store.product.dto.ProductCreateRequest;
import com.valentinpopescu.store.product.dto.ProductResponse;
import com.valentinpopescu.store.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "basicAuth")
@RestController
@Validated
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @Operation(summary = "Add product", description = "Add a product and return it")
    @ApiResponse(responseCode = "201", description = "Product created")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse add(@RequestBody @Valid ProductCreateRequest request) {
        return service.add(request);
    }

    @Operation(summary = "Find product by product code", description = "Find a product by product code and return it")
    @ApiResponse(responseCode = "200", description = "Product found")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{productCode}")
    public ProductResponse find(@PathVariable @NotBlank String productCode) {
        return service.findByProductCode(productCode);
    }

    @Operation(summary = "Find products", description = "List all products")
    @ApiResponse(responseCode = "200", description = "Products fetch successful")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public List<ProductResponse> findAll() {
        return service.findAll();
    }

    @Operation(summary = "Change price", description = "Find product by product code, change its price and return it")
    @ApiResponse(responseCode = "200", description = "Product's price changed")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/{productCode}/price")
    public ProductResponse changePrice(
            @PathVariable @NotBlank String productCode,
            @RequestBody @Valid PriceChangeRequest request) {
        return service.changePrice(productCode, request);
    }

    @Operation(summary = "Delete product", description = "Find product by product code and delete it")
    @ApiResponse(responseCode = "204", description = "Product deleted")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{productCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @NotBlank String productCode) {
        service.deleteByProductCode(productCode);
    }
}
