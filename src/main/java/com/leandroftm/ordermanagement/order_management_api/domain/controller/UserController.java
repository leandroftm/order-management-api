package com.leandroftm.ordermanagement.order_management_api.domain.controller;

import com.leandroftm.ordermanagement.order_management_api.domain.dto.response.OrderResponse;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.response.UserResponse;
import com.leandroftm.ordermanagement.order_management_api.domain.enums.Role;
import com.leandroftm.ordermanagement.order_management_api.domain.enums.Status;
import com.leandroftm.ordermanagement.order_management_api.security.entity.UserPrincipal;
import com.leandroftm.ordermanagement.order_management_api.service.OrderService;
import com.leandroftm.ordermanagement.order_management_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final OrderService orderService;

    //POST create user moved to auth

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(@RequestParam(required = false) Role role, @PageableDefault(size = 10, sort = "email") Pageable pageable) {
        return ResponseEntity.ok(userService.getUsers(role, pageable));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(userService.getUser(user.getId()));
    }

    //get by role merged with get all users

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/enable")
    public ResponseEntity<Void> enable(@PathVariable Long id) {
        userService.enable(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/disable")
    public ResponseEntity<Void> disable(@PathVariable Long id) {
        userService.disable(id);
        return ResponseEntity.noContent().build();
    }

    //REMOVED assign user role
//    @PostMapping("/{id}/role")
//    public ResponseEntity<Void> assignRole(@PathVariable Long id, @RequestBody UpdateUserRoleRequest request) {
//        userService.assignRole(id, request);
//        return ResponseEntity.noContent().build();
//    }

    @GetMapping("/me/orders")
    public ResponseEntity<Page<OrderResponse>> getOrdersByUser(@AuthenticationPrincipal UserPrincipal user, @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrdersByUser(user.getId(), pageable));
    }

    @GetMapping("/me/orders/status")
    public ResponseEntity<Page<OrderResponse>> getOrdersByUserAndStatus(@AuthenticationPrincipal UserPrincipal user, @RequestParam Status status, @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrdersByUserAndOrderStatus(user.getId(), status, pageable));
    }


    @GetMapping("/me/orders/{orderId}")
    public ResponseEntity<OrderResponse> getByUserId(@PathVariable Long orderId, @AuthenticationPrincipal UserPrincipal user) {
        OrderResponse response = orderService.findByIdAndUserId(orderId, user.getId());
        return ResponseEntity.ok(response);
    }
}
