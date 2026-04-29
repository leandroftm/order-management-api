package com.leandroftm.ordermanagement.order_management_api.domain.controller;

import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.create.CreateCategoryRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.create.CreateProductRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.update.UpdateCategoryRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.response.CategoryResponse;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.response.ProductResponse;
import com.leandroftm.ordermanagement.order_management_api.service.CategoryService;
import com.leandroftm.ordermanagement.order_management_api.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid CreateCategoryRequest request) {
        Long id = categoryService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{categoryId}/products")
    public ResponseEntity<Void> addProduct(@PathVariable Long categoryId, @RequestBody @Valid CreateProductRequest request) {
        Long id = productService.create(categoryId, request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }


    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> getAll(@PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<CategoryResponse> page = categoryService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Long id) {
        CategoryResponse categoryResponse = categoryService.getById(id);
        return ResponseEntity.ok(categoryResponse);
    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<Page<ProductResponse>> getProductsByCategory(@PathVariable Long categoryId, Pageable pageable) {
        Page<ProductResponse> page = productService.findAllProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(page);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid UpdateCategoryRequest request) {
        categoryService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/enable")
    public ResponseEntity<Void> enable(@PathVariable Long id) {
        categoryService.enableCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/disable")
    public ResponseEntity<Void> disable(@PathVariable Long id) {
        categoryService.disableCategory(id);
        return ResponseEntity.noContent().build();
    }
}
