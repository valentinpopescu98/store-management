package com.valentinpopescu.store.product.service;

import com.valentinpopescu.store.product.model.Product;
import com.valentinpopescu.store.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Supplier;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    public static final Supplier<EntityNotFoundException> PRODUCT_NOT_FOUND =
            () -> new EntityNotFoundException("Product not found");
    private final ProductRepository repository;

    @Override
    public Product add(Product product) throws BadRequestException {
        if (repository.existsByProductCode(product.getProductCode())) {
            throw new BadRequestException("Product already exists");
        }

        Product savedProduct = repository.save(product);
        log.info("Product created: product code={}", savedProduct.getProductCode());

        return savedProduct;
    }

    @Override
    public Product findByProductCode(String productCode) {
        return repository.findByProductCode(productCode)
                .orElseThrow(PRODUCT_NOT_FOUND);
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Override
    public Product changePrice(String productCode, BigDecimal price) {
        Product product = repository.findByProductCode(productCode)
                .orElseThrow(PRODUCT_NOT_FOUND);

        product.setPrice(price);
        log.info("Price changed: product code={}, new price={}", productCode, price);
        return product;
    }

    @Override
    public void deleteByProductCode(String productCode) {
        Product product = repository.findByProductCode(productCode)
                .orElseThrow(PRODUCT_NOT_FOUND);

        repository.delete(product);
        log.warn("Product deleted: product code={}", productCode);
    }
}
