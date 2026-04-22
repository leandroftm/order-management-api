package com.leandroftm.ordermanagement.order_management_api.service;

import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.CreateProductRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.response.ProductResponse;
import com.leandroftm.ordermanagement.order_management_api.domain.entity.Category;
import com.leandroftm.ordermanagement.order_management_api.domain.entity.Product;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.category.CategoryNotFoundException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.category.CategoryNullException;
import com.leandroftm.ordermanagement.order_management_api.repository.CategoryRepository;
import com.leandroftm.ordermanagement.order_management_api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    public ProductResponse createProduct(CreateProductRequest request, Long categoryId) {
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
        return new ProductResponse(savedProduct);
    }

}
