package com.leandroftm.ordermanagement.order_management_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.update.UpdatePriceRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.update.UpdateProductRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.response.ProductResponse;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnAllProductsSuccessfully() throws Exception {
        Page<ProductResponse> page = new PageImpl<>(List.of(
                new ProductResponse(
                        "Product Test",
                        "",
                        new BigDecimal("100.00"),
                        1L,
                        "Category Test"
                )
        ));

        when(productService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(page)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].name").value("Product Test"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnProductSuccessfully() throws Exception {
        ProductResponse response = new ProductResponse(
                "Product Test",
                "",
                new BigDecimal("100.00"),
                1L,
                "Category Test"
        );

        when(productService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(response)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product Test"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateProductSuccessfully() throws Exception {
        UpdateProductRequest request = new UpdateProductRequest(
                "Product Test",
                ""
        );
        mockMvc.perform(put("/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateProductPriceSuccessfully() throws Exception {
        UpdatePriceRequest request = new UpdatePriceRequest(
                new BigDecimal("150.00")
        );

        mockMvc.perform(patch("/products/{id}/price", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldIncreaseProductStockSuccessfully() throws Exception {
        mockMvc.perform(post("/products/{id}/stock/increase", 1L)
                        .param("quantity", "100"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDecreaseProductStockSuccessfully() throws Exception {
        mockMvc.perform(post("/products/{id}/stock/decrease", 1L)
                        .param("quantity", "100"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldEnableProductSuccessfully() throws Exception {
        mockMvc.perform(post("/products/{id}/enable", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDisableProductSuccessfully() throws Exception {
        mockMvc.perform(post("/products/{id}/enable", 1L))
                .andExpect(status().isNoContent());
    }
}
