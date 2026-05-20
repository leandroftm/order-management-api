package com.leandroftm.ordermanagement.order_management_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.create.CreateCategoryRequest;
import com.leandroftm.ordermanagement.order_management_api.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerAdviceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbiddenWhenUserIsNotAdmin() throws Exception {
        mockMvc.perform(post("/categories/{categoryId}/disable", 1L))
                .andExpect(status().isForbidden());
        verifyNoInteractions(categoryService);
    }

}
