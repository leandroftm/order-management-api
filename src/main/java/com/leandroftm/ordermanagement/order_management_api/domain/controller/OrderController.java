package com.leandroftm.ordermanagement.order_management_api.domain.controller;

import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.create.CreateOrderRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.response.OrderResponse;
import com.leandroftm.ordermanagement.order_management_api.domain.enums.Status;
import com.leandroftm.ordermanagement.order_management_api.service.OrderService;
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
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestParam Long userId, @RequestBody @Valid CreateOrderRequest request) {
        Long id = orderService.createOrder(userId, request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    //will receive user from authorization later
    @GetMapping("/user/{userId}/status")
    public ResponseEntity<Page<OrderResponse>> getByUserAndStatus(@PathVariable Long userId, @RequestParam Status status, @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrdersByUserAndOrderStatus(userId, status, pageable));
    }

    //will receive user from authorization later
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<OrderResponse>> getByUser(@PathVariable Long userId, @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<Void> pay(@PathVariable Long id) {
        orderService.payOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }

    //ADMIN
    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getAll(@PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }
}