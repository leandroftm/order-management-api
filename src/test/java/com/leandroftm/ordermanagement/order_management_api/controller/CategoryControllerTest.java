package com.leandroftm.ordermanagement.order_management_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.create.CreateCategoryRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.create.CreateProductRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.update.UpdateCategoryRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.response.CategoryResponse;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.response.ProductResponse;
import com.leandroftm.ordermanagement.order_management_api.service.CategoryService;
import com.leandroftm.ordermanagement.order_management_api.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Long categoryId = 1L;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateCategorySuccessfully() throws Exception {
        CreateCategoryRequest request = new CreateCategoryRequest(
                "Category Test"
        );

        when(categoryService.create(request)).thenReturn(1L);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAddProductSuccessfully() throws Exception {
        CreateProductRequest request = new CreateProductRequest(
                "Product Test",
                "",
                new BigDecimal("100.00"),
                500
        );

        when(productService.create(categoryId, request)).thenReturn(1L);

        mockMvc.perform(post("/categories/{categoryId}/products", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateCategorySuccessfully() throws Exception {
        UpdateCategoryRequest request = new UpdateCategoryRequest(
                "New Category Name Test"
        );

        mockMvc.perform(patch("/categories/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldEnableCategorySuccessfully() throws Exception {
        mockMvc.perform(post("/categories/{categoryId}/enable", categoryId))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDisableCategorySuccessfully() throws Exception {
        mockMvc.perform(post("/categories/{categoryId}/disable", categoryId))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbiddenWhenAuthorizationDenied() throws Exception {
        mockMvc.perform(post("/categories/{categoryId}/disable", categoryId))
                .andExpect(status().isForbidden());
        verifyNoInteractions(categoryService);
    }

    //# END ADMIN

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnAllCategoriesSuccessfully() throws Exception {
        Page<CategoryResponse> page = new PageImpl<>(List.of());
        when(categoryService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnCategorySuccessfully() throws Exception {
        CategoryResponse response = new CategoryResponse(
                "Category Test",
                List.of()
        );

        when(categoryService.findById(categoryId)).thenReturn(response);

        mockMvc.perform(get("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(response)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Category Test"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnProductsByCategorySuccessfully() throws Exception {
        Page<ProductResponse> page = new PageImpl<>(List.of(new ProductResponse(
                "Product test",
                "",
                new BigDecimal("100.00"),
                categoryId,
                "Category Test"
        )));

        when(productService.findAllProductsByCategory(eq(categoryId), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/categories/{categoryId}/products", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(page)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].name").value("Product test"));
    }
}
