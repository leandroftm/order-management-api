package com.leandroftm.ordermanagement.order_management_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.update.UpdateUserRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.response.OrderResponse;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.response.UserResponse;
import com.leandroftm.ordermanagement.order_management_api.domain.entity.User;
import com.leandroftm.ordermanagement.order_management_api.domain.enums.Role;
import com.leandroftm.ordermanagement.order_management_api.domain.enums.Status;
import com.leandroftm.ordermanagement.order_management_api.security.entity.UserPrincipal;
import com.leandroftm.ordermanagement.order_management_api.service.OrderService;
import com.leandroftm.ordermanagement.order_management_api.service.UserService;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnAllUsersSuccessfully() throws Exception {
        Page<UserResponse> page = new PageImpl<>(List.of(
                new UserResponse(
                        1L,
                        "test@test.com",
                        "123456",
                        true,
                        LocalDateTime.now().minusDays(10L),
                        LocalDateTime.now().minusDays(10L),
                        Role.ADMIN,
                        List.of()
                )));

        when(userService.getUsers(any(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(page)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[*].id").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnAllUsersByRoleSuccessfully() throws Exception {
        Page<UserResponse> page = new PageImpl<>(List.of(
                new UserResponse(
                        1L,
                        "test@test.com",
                        "123456",
                        true,
                        LocalDateTime.now().minusDays(10L),
                        LocalDateTime.now().minusDays(10L),
                        Role.ADMIN,
                        List.of()
                )));

        when(userService.getUsers(any(Role.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/users")
                        .param("role", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(page)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[*].id").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldEnableUserSuccessfully() throws Exception {
        mockMvc.perform(post("/users/{userId}/enable", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDisableUserSuccessfully() throws Exception {
        mockMvc.perform(post("/users/{userId}/disable", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnUserMeSuccessfully() throws Exception {
        UserResponse response = new UserResponse(
                1L,
                "test@test.com",
                "123456",
                true,
                LocalDateTime.now(),
                LocalDateTime.now(),
                Role.USER,
                List.of(//orders
                ));

        User user = createUser();
        UserPrincipal userPrincipal = new UserPrincipal(user);

        when(userService.getUser(1L)).thenReturn(response);

        mockMvc.perform(get("/users/me")
                        .with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(response)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldUpdateUserSuccessfully() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest(
                "test@test.com",
                "123456"
        );

        User user = createUser();
        UserPrincipal userPrincipal = new UserPrincipal(user);

        mockMvc.perform(put("/users/me")
                        .with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnOrderByUserSuccessfully() throws Exception {
        OrderResponse response = new OrderResponse(
                1L,
                new BigDecimal("500.00"),
                LocalDateTime.now().minusDays(1L),
                Status.CREATED,
                1L,
                List.of()
        );

        User user = createUser();
        UserPrincipal userPrincipal = new UserPrincipal(user);

        when(orderService.findByIdAndUserId(eq(1L), eq(1L))).thenReturn(response);

        mockMvc.perform(get("/users/me/orders/{orderId}", 1L)
                        .with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(response)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnOrdersByUserSuccessfully() throws Exception {
        Page<OrderResponse> page = createPage();

        User user = createUser();
        UserPrincipal userPrincipal = new UserPrincipal(user);

        when(orderService.getOrdersByUser(eq(1L), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/users/me/orders")
                        .with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(page)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].id").value(1));
    }

    @Test
    @WithMockUser(roles = ("USER"))
    void shouldReturnOrdersByUserAndStatusSuccessfully() throws Exception {
        Page<OrderResponse> page = createPage();

        User user = createUser();
        UserPrincipal userPrincipal = new UserPrincipal(user);

        when(orderService.getOrdersByUserAndOrderStatus(eq(1L), any(Status.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/users/me/orders/status")
                        .with(user(userPrincipal))
                        .param("status", "CREATED")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(page)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[*].id").value(1))
                .andExpect(jsonPath("$.content[*].userId").value(1));
    }

    private User createUser() {
        User user = new User("email@test.com", "123456", Role.USER);
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    private Page<OrderResponse> createPage() {
        return new PageImpl<>(
                List.of(new OrderResponse(
                        1L,
                        new BigDecimal("500.00"),
                        LocalDateTime.now().minusDays(1L),
                        Status.CREATED,
                        1L,
                        List.of()
                )));
    }
}
