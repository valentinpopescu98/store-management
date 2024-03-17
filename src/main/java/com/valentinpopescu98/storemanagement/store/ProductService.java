package com.valentinpopescu98.storemanagement.store;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Product getOne(Long id) {
        Optional<Product> foundProduct = repository.findById(id);
        boolean productNotFound = foundProduct.isEmpty();

        // if the id from the request returns no result, throw exception
        if (productNotFound) {
            throw new IllegalStateException(
                    String.format("Product with %d ID not found.", id));
        }

        return foundProduct.get();
    }

    public List<Product> getAll() {
        return repository.findAll();
    }

    public Product add(Product product) {
        Optional<Product> foundProduct = repository.findByName(product.getName());
        boolean productExists = foundProduct.isPresent();

        // if a product with the same name exists
        if (productExists) {
            throw new IllegalStateException(
                    String.format("Product with name %s exists.", product.getName()));
        }

        if (product.getName() == null) {
            throw new IllegalStateException("Product must have a name");
        }
        if (product.getDescription() == null) {
            throw new IllegalStateException("Product must have a description");
        }
        if (product.getPrice() == null) {
            throw new IllegalStateException("Product must have a price value");
        }
        if (product.getPrice() == 0) {
            throw new IllegalStateException("Product must have a price bigger than 0");
        }
        if (product.getStock() == null) {
            throw new IllegalStateException("Product must have a stock number");
        }

        repository.save(product);

        return product;
    }

    public Product update(Long id, Product newProduct) {
        Optional<Product> oldProductOptional = repository.findById(id);
        Optional<Product> newProductOptional = repository.findByName(newProduct.getName());

        boolean oldProductNotFound = oldProductOptional.isEmpty();
        boolean newProductExists = newProductOptional.isPresent();

        if (oldProductNotFound) {
            throw new IllegalStateException(
                    String.format("Product with ID %d not found.", id));
        }

        if (newProductExists) {
            throw new IllegalStateException(
                    String.format("Product with name %s already exists.", newProduct.getName()));
        }

        if (newProduct.getPrice() != null && newProduct.getPrice() == 0) {
            throw new IllegalStateException("Product must have a price bigger than 0");
        }

        Product oldProduct = updateOldDto(oldProductOptional.get(), newProduct);

        repository.save(oldProduct);
        return oldProduct;
    }

    public String remove(Long id) {
        Optional<Product> foundProduct = repository.findById(id);
        boolean productNotFound = foundProduct.isEmpty();

        if (productNotFound) {
            throw new IllegalStateException(
                    String.format("Product with %d ID not found.", id));
        }

        Product product = foundProduct.get();
        repository.delete(product);

        return product + " deleted";
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
