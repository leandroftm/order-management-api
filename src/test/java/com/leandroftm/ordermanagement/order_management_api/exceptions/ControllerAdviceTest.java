package com.leandroftm.ordermanagement.order_management_api.exceptions;

import com.leandroftm.ordermanagement.order_management_api.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(GlobalExceptionHandler.class)
public class ControllerAdviceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/tests/bad-request"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("CATEGORY_NOT_EMPTY"))
                .andExpect(jsonPath("$.messages").value("Category is not empty"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnValidationException() throws Exception {
        mockMvc.perform(get("/tests/validation"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_EXCEPTION"))
                .andExpect(jsonPath("$.messages").value("Validation Exception"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnNotFoundException() throws Exception {
        mockMvc.perform(get("/tests/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"))
                .andExpect(jsonPath("$.messages").value("Not Found Exception"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnConflictException() throws Exception {
        mockMvc.perform(get("/tests/conflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("CONFLICT"))
                .andExpect(jsonPath("$.messages").value("Conflict Exception"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnAuthorizationDeniedForbiddenException() throws Exception {
        mockMvc.perform(get("/tests/authorization-denied"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.messages").value("Authorization Denied"))
                .andExpect(jsonPath("$.errorCode").value("INVALID_CREDENTIALS"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnAccessDeniedDeniedForbiddenException() throws Exception {
        mockMvc.perform(get("/tests/access-denied"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.messages").value("Access Denied"))
                .andExpect(jsonPath("$.errorCode").value("INVALID_CREDENTIALS"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnGenericException() throws Exception {
        mockMvc.perform(get("/tests/generic"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value("UNEXPECTED_ERROR"))
                .andExpect(jsonPath("$.messages").value("Unexpected error"));
    }
}
