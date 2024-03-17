package com.valentinpopescu98.storemanagement.store;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping(path = "{productId}")
    public Product getOne(@PathVariable("productId") Long id) {
        return service.getOne(id);
    }

    @GetMapping
    public List<Product> getAll() {
        return service.getAll();
    }

    @PostMapping
    public Product add(@RequestBody Product product) {
        return service.add(product);
    }

    @PutMapping(path = "{productId}")
    public Product update(@PathVariable("productId") Long id, @RequestBody Product newProduct) {
        return service.update(id, newProduct);
    }

    @DeleteMapping(path = "{productId}")
    public String remove(@PathVariable("productId") Long id) {
        return service.remove(id);
    }

}
