package com.valentinpopescu98.storemanagement.store;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Product getOne(Long id) {
        Optional<Product> fetchedProduct = repository.findById(id);
        boolean productNotFound = fetchedProduct.isEmpty();

        // if the id from the request returns no result
        if (productNotFound) {
            throw new EntityNotFoundException(
                    String.format("Product with ID '%d' not found.", id));
        }

        return fetchedProduct.get();
    }

    public List<Product> getAll() {
        List<Product> fetchedProducts = repository.findAll();
        boolean noProductFound = fetchedProducts.isEmpty();

        // if the database responds with no product
        if (noProductFound) {
            throw new EmptyResultDataAccessException("No product has been found", 1);
        }

        return fetchedProducts;
    }

    public Product add(Product product) {
        String productName = product.getName();
        boolean nameAlreadyExisting = repository.existsByName(productName);

        // if a product with the same name exists in the DB
        if (nameAlreadyExisting) {
            throw new DataIntegrityViolationException(
                    String.format("Product with name '%s' already exists.", productName));
        }

        // if the added product has conflicting fields
        if (productName == null) {
            throw new IllegalArgumentException("Product must have a name");
        }
        if (product.getDescription() == null) {
            throw new IllegalArgumentException("Product must have a description");
        }
        if (product.getPrice() == null) {
            throw new IllegalArgumentException("Product must have a price value");
        }
        if (product.getPrice() == 0) {
            throw new IllegalArgumentException("Product must have a price bigger than '0'");
        }
        if (product.getStock() == null) {
            throw new IllegalArgumentException("Product must have a stock number");
        }

        return repository.save(product);
    }

    public Product update(Long id, Product newProduct) {
        Optional<Product> oldProductOptional = repository.findById(id);
        String productName = newProduct.getName();
        boolean productNotFound = oldProductOptional.isEmpty();
        boolean nameAlreadyExisting = repository.existsByName(productName);

        // if the id from the request returns no result
        if (productNotFound) {
            throw new EntityNotFoundException(
                    String.format("Product with ID '%d' not found.", id));
        }

        // if a product with the same name exists in the DB
        if (nameAlreadyExisting) {
            throw new DataIntegrityViolationException(
                    String.format("Product with name '%s' already exists.", productName));
        }

        // if the updated product price is '0'
        if (newProduct.getPrice() != null && newProduct.getPrice() == 0) {
            throw new IllegalArgumentException("Product must have a price bigger than '0'");
        }

        Product oldProduct = updateOldDto(oldProductOptional.get(), newProduct);
        return repository.save(oldProduct);
    }

    public void remove(Long id) {
        Optional<Product> foundProduct = repository.findById(id);
        boolean productNotFound = foundProduct.isEmpty();

        if (productNotFound) {
            throw new EntityNotFoundException(
                    String.format("Product with ID '%d' not found.", id));
        }

        Product product = foundProduct.get();
        repository.delete(product);
    }

    private static Product updateOldDto(Product oldProduct, Product newProduct) {
        if (newProduct.getName() != null) {
            oldProduct.setName(newProduct.getName());
        }
        if (newProduct.getDescription() != null) {
            oldProduct.setDescription(newProduct.getDescription());
        }
        if (newProduct.getPrice() != null) {
            oldProduct.setPrice(newProduct.getPrice());
        }
        oldProduct.setMsrp(newProduct.getMsrp());
        if (newProduct.getStock() != null) {
            oldProduct.setStock(newProduct.getStock());
        }

        return oldProduct;
    }

}
