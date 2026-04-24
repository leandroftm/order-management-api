package com.leandroftm.ordermanagement.order_management_api.service;

import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.create.CreateCategoryRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.update.UpdateCategoryRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.response.CategoryResponse;
import com.leandroftm.ordermanagement.order_management_api.domain.entity.Category;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.category.CategoryAlreadyExistsException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.category.CategoryNotEmptyException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.category.CategoryNotFoundException;
import com.leandroftm.ordermanagement.order_management_api.repository.CategoryRepository;
import com.leandroftm.ordermanagement.order_management_api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    public Long create(CreateCategoryRequest request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new CategoryAlreadyExistsException();
        }

        return categoryRepository.save(new Category(request.name())).getId();
    }

    @Transactional(readOnly = true)
    public Page<CategoryResponse> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(CategoryResponse::new);
    }

    public void update(Long id, UpdateCategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        if (!category.getName().equals(request.name()) &&
                categoryRepository.existsByName(request.name())) {
            throw new CategoryAlreadyExistsException();
        }

        category.update(request.name());
    }

    public void deactivate(Long id) {

        if (productRepository.existsByCategoryId(id)) {
            throw new CategoryNotEmptyException();
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        category.disable();
    }

    public void activate(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        category.enable();
    }
}
