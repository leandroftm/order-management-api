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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CategoryService categoryService;

    private final long id = 10L;

    @Test
    void shouldCreateCategorySuccessfully() {
        CreateCategoryRequest request = new CreateCategoryRequest(
                "Category test"
        );

        Category savedCategory = new Category("Category test");
        ReflectionTestUtils.setField(savedCategory, "id", id);

        when(categoryRepository.existsByNameIgnoreCase(request.name())).thenReturn(false);

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        long createdId = categoryService.create(request);

        assertEquals(createdId, savedCategory.getId());

        verify(categoryRepository, times(1)).existsByNameIgnoreCase(request.name());

        verify(categoryRepository).save(argThat(category ->
                category.getName().equals("Category test")));

        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void shouldReturnAllCategoriesSuccessfully() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = List.of(new Category("Category test"));
        Page<Category> page = new PageImpl<>(categories, pageable, categories.size());

        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<CategoryResponse> responses = categoryService.findAll(pageable);

        assertNotNull(responses);
        assertEquals("Category test", responses.getContent().get(0).name());

        verify(categoryRepository).findAll(pageable);
    }

    @Test
    void shouldReturnCategorySuccessfully() {
        Category category = new Category("Category test");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        CategoryResponse response = categoryService.getById(id);

        assertNotNull(response);
        assertEquals("Category test", response.name());
        verify(categoryRepository).findById(id);
    }

    @Test
    void shouldUpdateCategorySuccessfully() {
        UpdateCategoryRequest request = new UpdateCategoryRequest(
                "Updated Category test"
        );
        Category savedCategory = new Category("Saved Category test");
        ReflectionTestUtils.setField(savedCategory, "id", id);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(savedCategory));

        when(categoryRepository.existsByNameIgnoreCase(request.name())).thenReturn(false);

        categoryService.update(id, request);

        verify(categoryRepository).findById(id);
        verify(categoryRepository).existsByNameIgnoreCase(request.name());
        verify(categoryRepository).save(argThat(category ->
                category.getName().equals("Updated Category test")));

        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void shouldEnableCategorySuccessfully() {
        Category savedCategory = new Category("Saved Category test");
        ReflectionTestUtils.setField(savedCategory, "id", id);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(savedCategory));

        assertFalse(savedCategory.isActive());

        categoryService.enableCategory(id);

        assertTrue(savedCategory.isActive());
        verify(categoryRepository).findById(id);
        verify(categoryRepository).save(savedCategory);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void shouldDisableCategorySuccessfully() {
        Category savedCategory = new Category("Saved Category test");
        ReflectionTestUtils.setField(savedCategory, "id", id);
        savedCategory.enable();

        when(productRepository.existsByCategoryId(id)).thenReturn(false);
        when(categoryRepository.findById(id)).thenReturn(Optional.of(savedCategory));


        assertTrue(savedCategory.isActive());
        categoryService.disableCategory(id);

        assertFalse(savedCategory.isActive());
        verify(categoryRepository).findById(id);
        verify(categoryRepository).save(savedCategory);
        verifyNoMoreInteractions(categoryRepository, productRepository);
    }

    //#Excentions

    @Test
    void shouldThrowExceptionWhenOnCreateCategoryNameAlreadyExists() {
        CreateCategoryRequest request = new CreateCategoryRequest(
                "Category test"
        );

        when(categoryRepository.existsByNameIgnoreCase(request.name())).thenReturn(true);

        assertThrows(CategoryAlreadyExistsException.class,
                () -> categoryService.create(request));

        verify(categoryRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenOnUpdateCategoryNameAlreadyExists() {
        UpdateCategoryRequest request = new UpdateCategoryRequest(
                "New Category test"
        );
        Category savedCategory = new Category("Saved Category test");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(savedCategory));

        when(categoryRepository.existsByNameIgnoreCase(request.name())).thenReturn(true);

        assertThrows(CategoryAlreadyExistsException.class,
                () -> categoryService.update(id, request));

        verify(categoryRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.getById(id));

        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void shouldThrowExceptionWhenDisablingNotEmptyCategory() {
        when(productRepository.existsByCategoryId(id)).thenReturn(true);

        assertThrows(CategoryNotEmptyException.class,
                () -> categoryService.disableCategory(id));

        verifyNoMoreInteractions(categoryRepository, productRepository);
    }
}
