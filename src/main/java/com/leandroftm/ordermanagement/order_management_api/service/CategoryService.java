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
        if (categoryRepository.existsByNameIgnoreCase(request.name())) {
            throw new CategoryAlreadyExistsException();
        }

        return categoryRepository.save(new Category(request.name())).getId();
    }

    @Transactional(readOnly = true)
    public Page<CategoryResponse> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(CategoryResponse::new);
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        return categoryRepository.findById(id).map(CategoryResponse::new)
                .orElseThrow(CategoryNotFoundException::new);
    }

    public void update(Long id, UpdateCategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        if (categoryRepository.existsByNameIgnoreCaseAndIdNot(request.name(), id)) {
            throw new CategoryAlreadyExistsException();
        }

        category.update(request.name());
        categoryRepository.save(category);
    }

    public void disableCategory(Long id) {

        if (productRepository.existsByCategoryId(id)) {
            throw new CategoryNotEmptyException();
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        category.disable();
        categoryRepository.save(category);
    }

    public void enableCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        category.enable();
        categoryRepository.save(category);
    }
}
