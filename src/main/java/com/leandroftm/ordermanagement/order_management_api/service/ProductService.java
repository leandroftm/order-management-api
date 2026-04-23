package com.leandroftm.ordermanagement.order_management_api.service;

import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.CreateProductRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.UpdateProductRequest;
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

    public ProductResponse create(Long categoryId, CreateProductRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);

        validate(request);

        Product product = Product.create(
                request.name(),
                request.description(),
                request.price(),
                request.stock(),
                category
        );

        Product savedProduct = productRepository.save(product);
        return new ProductResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductResponse::new);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAllProductsByCategory(Long categoryId, Pageable pageable) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException();
        }
        return productRepository.findAllByCategory(categoryId);
    }

    public void update(Long categoryId, Long productId, UpdateProductRequest request) {
        Product product = productRepository.findByIdAndCategoryId(productId, categoryId)
                .orElseThrow(ProductNotFoundException::new);

        product.updateDetails(request.name(), request.description());
    }

    public void updatePrice(Long categoryId, Long productId, BigDecimal newPrice) {
        if (newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidProductPriceException();
        }

        Product product = productRepository.findByIdAndCategoryId(productId, categoryId)
                .orElseThrow(ProductNotFoundException::new);

        product.updatePrice(newPrice);
    }

    public void increaseStock(Long categoryId, Long productId, Integer amount) {
        if (amount <= 0) {
            throw new InvalidStockAmountException();
        }

        Product product = productRepository.findByIdAndCategoryId(productId, categoryId)
                .orElseThrow(ProductNotFoundException::new);

        product.increaseStock(amount);
    }

    public void decreaseStock(Long categoryId, Long productId, Integer amount) {
        if (amount <= 0) {
            throw new InvalidStockAmountException();
        }

        Product product = productRepository.findByIdAndCategoryId(productId, categoryId)
                .orElseThrow(ProductNotFoundException::new);

        product.decreaseStock(amount);
    }

    public void disableProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        if (!product.isActive()) {
            throw new ProductAlreadyInactiveException();
        }
        product.disable();
    }

    public void enableProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        if (product.isActive()) {
            throw new ProductAlreadyActiveException();
        }
        product.enable();
    }

    private void validate(CreateProductRequest request) {
        if (request.price().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidProductPriceException();
        }

        if (request.stock() < 0) {
            throw new InvalidStockException();
        }
    }
}