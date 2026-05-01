package com.leandroftm.ordermanagement.order_management_api.domain.controller;

import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.create.CreateUserRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.update.UpdateUserRoleRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.response.OrderResponse;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.response.UserResponse;
import com.leandroftm.ordermanagement.order_management_api.domain.enums.Role;
import com.leandroftm.ordermanagement.order_management_api.domain.enums.Status;
import com.leandroftm.ordermanagement.order_management_api.service.OrderService;
import com.leandroftm.ordermanagement.order_management_api.service.UserService;
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
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateUserRequest request) {
        Long id = userService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAll(@PageableDefault(size = 10, sort = "email") Pageable pageable) {
        return ResponseEntity.ok(userService.getUsers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping("/role")
    public ResponseEntity<Page<UserResponse>> getByRole(@RequestParam Role role, @PageableDefault(size = 10, sort = "email") Pageable pageable) {
        return ResponseEntity.ok(userService.getUsersByRole(role, pageable));
    }

    @PostMapping("/{id}/enable")
    public ResponseEntity<Void> enable(@PathVariable Long id) {
        userService.enable(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/disable")
    public ResponseEntity<Void> disable(@PathVariable Long id) {
        userService.disable(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/role")
    public ResponseEntity<Void> assignRole(@PathVariable Long id, @RequestBody UpdateUserRoleRequest request) {
        userService.assignRole(id, request);
        return ResponseEntity.noContent().build();
    }

    //will receive user from authorization later
    @GetMapping("/{id}/orders")
    public ResponseEntity<Page<OrderResponse>> getOrderByUser(@PathVariable Long id, @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrdersByUser(id, pageable));
    }

    //will receive user from authorization later
    @GetMapping("/{id}/orders/status")
    public ResponseEntity<Page<OrderResponse>> getByUserAndStatus(@PathVariable Long id, @RequestParam Status status, @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrdersByUserAndOrderStatus(id, status, pageable));
    }
}
