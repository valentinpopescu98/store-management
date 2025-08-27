package com.valentinpopescu.store.product.service;

import com.valentinpopescu.store.exceptions.BadRequestException;
import com.valentinpopescu.store.exceptions.NotFoundException;
import com.valentinpopescu.store.product.dto.PriceChangeRequest;
import com.valentinpopescu.store.product.dto.ProductCreateRequest;
import com.valentinpopescu.store.product.dto.ProductResponse;
import com.valentinpopescu.store.product.model.Product;
import com.valentinpopescu.store.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    public static final Supplier<NotFoundException> PRODUCT_NOT_FOUND =
            () -> new NotFoundException("Product not found");
    private final ProductRepository repository;

    @Override
    public ProductResponse add(ProductCreateRequest request) {
        if (repository.existsByProductCode(request.productCode())) {
            throw new BadRequestException("Product already exists");
        }

        Product product = new Product(request.productCode(), request.name(), request.price());
        Product savedProduct = repository.save(product);
        log.info("Product created: product code={}", savedProduct.getProductCode());

        return map(savedProduct);
    }

    @Override
    public ProductResponse findByProductCode(String productCode) {
        Product product = repository.findByProductCode(productCode)
                .orElseThrow(PRODUCT_NOT_FOUND);
        return map(product);
    }

    @Override
    public List<ProductResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public ProductResponse changePrice(String productCode, PriceChangeRequest request) {
        Product product = repository.findByProductCode(productCode)
                .orElseThrow(PRODUCT_NOT_FOUND);

        product.setPrice(request.price());
        log.info("Price changed: product code={}, new price={}", productCode, request.price());
        return map(product);
    }

    @Override
    public void deleteByProductCode(String productCode) {
        Product product = repository.findByProductCode(productCode)
                .orElseThrow(PRODUCT_NOT_FOUND);

        repository.delete(product);
        log.warn("Product deleted: product code={}", productCode);
    }

    private ProductResponse map(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getProductCode(),
                product.getName(),
                product.getPrice()
        );
    }
}
