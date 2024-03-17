package com.valentinpopescu98.storemanagement.store;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping("/{productId}")
    public ResponseEntity<?> getOne(@PathVariable("productId") Long id) {
        try {
            Product fetchedProduct = service.getOne(id);
            return ResponseEntity
                    .ok(fetchedProduct);
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Product> fetchedProducts = service.getAll();
            return ResponseEntity
                    .ok(fetchedProducts);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Product product) {
        try {
            Product savedProduct = service.add(product);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(savedProduct);
        } catch (DataIntegrityViolationException | IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> update(@PathVariable("productId") Long id, @RequestBody Product newProduct) {
        try {
            Product updatedProduct = service.update(id, newProduct);
            return ResponseEntity
                    .ok(updatedProduct);
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (DataIntegrityViolationException | IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> remove(@PathVariable("productId") Long id) {
        try {
            service.remove(id);
            return ResponseEntity
                    .noContent()
                    .build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

}
