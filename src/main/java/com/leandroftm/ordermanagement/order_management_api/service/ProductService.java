package com.leandroftm.ordermanagement.order_management_api.service;

import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.create.CreateProductRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.update.UpdatePriceRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.update.UpdateProductRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.response.ProductResponse;
import com.leandroftm.ordermanagement.order_management_api.domain.entity.Category;
import com.leandroftm.ordermanagement.order_management_api.domain.entity.Product;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.category.CategoryNotFoundException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.product.*;
import com.leandroftm.ordermanagement.order_management_api.repository.CategoryRepository;
import com.leandroftm.ordermanagement.order_management_api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    public Long create(Long categoryId, CreateProductRequest request) {
        validate(request);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);

        Product product = Product.create(
                request.name(),
                request.description(),
                request.price(),
                request.stock(),
                category
        );

        Product savedProduct = productRepository.save(product);
        return savedProduct.getId();
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductResponse::new);
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        return new ProductResponse(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAllProductsByCategory(Long categoryId, Pageable pageable) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException();
        }
        return productRepository.findAllByCategory(categoryId, pageable);
    }

    //ADMIN ROLE
    public void update(Long productId, UpdateProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        if (productRepository.existsByNameAndIdNot(request.name(), productId)) {
            throw new ProductAlreadyExistsException();
        }

        product.updateDetails(request.name(), request.description());
        productRepository.save(product);
    }

    //PATCH

    public void updatePrice(Long productId, UpdatePriceRequest request) {
        BigDecimal newPrice = request.price();
        if (newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidProductPriceException();
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        product.updatePrice(newPrice);
        productRepository.save(product);
    }

    //POST

    public void increaseStock(Long productId, Integer amount) {
        if (amount <= 0) {
            throw new InvalidStockAmountException();
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        product.increaseStock(amount);
        productRepository.save(product);
    }

    public void decreaseStock(Long productId, Integer amount) {
        if (amount <= 0) {
            throw new InvalidStockAmountException();
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        product.decreaseStock(amount);
        productRepository.save(product);
    }

    public void disableProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        if (!product.isActive()) {
            throw new ProductAlreadyInactiveException();
        }
        product.disable();
        productRepository.save(product);
    }

    public void enableProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        if (product.isActive()) {
            throw new ProductAlreadyActiveException();
        }
        product.enable();
        productRepository.save(product);
    }
//END ADMIN ROLE

    private void validate(CreateProductRequest request) {
        if (request.price().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidProductPriceException();
        }

        if (request.stock() < 0) {
            throw new InvalidStockException();
        }
    }
}