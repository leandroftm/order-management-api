package com.leandroftm.ordermanagement.order_management_api.service;

import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.create.CreateOrderRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.response.OrderResponse;
import com.leandroftm.ordermanagement.order_management_api.domain.entity.Order;
import com.leandroftm.ordermanagement.order_management_api.domain.entity.OrderItem;
import com.leandroftm.ordermanagement.order_management_api.domain.entity.Product;
import com.leandroftm.ordermanagement.order_management_api.domain.entity.User;
import com.leandroftm.ordermanagement.order_management_api.domain.enums.Status;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.order.InvalidOrderException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.order.InvalidOrderStatusException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.order.OrderNotFoundException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.product.InsuficientStockException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.product.ProductNotFoundException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.user.UserNotFoundException;
import com.leandroftm.ordermanagement.order_management_api.repository.OrderRepository;
import com.leandroftm.ordermanagement.order_management_api.repository.ProductRepository;
import com.leandroftm.ordermanagement.order_management_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    public Long createOrder(Long userId, CreateOrderRequest request) {
        //fill order items with products
        List<OrderItem> items = findOrderItems(request);

        if (items.isEmpty()) {
            throw new InvalidOrderException();
        }

        for (OrderItem item : items) {
            if (item.getProduct().getStock() < item.getQuantity()) {
                throw new InsuficientStockException();
            }
        }

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Order order = new Order();
        order.create(user, items);

        Order savedOrder = orderRepository.save(order);
        return savedOrder.getId();
    }

    //GET ADMIN
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(OrderResponse::new);
    }
    //END GET ADMIN

    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrdersByUserAndOrderStatus(Long userId, Status status, Pageable pageable) {
        return orderRepository.findByUserIdAndStatus(userId, status, pageable).map(OrderResponse::new);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrdersByUser(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable).map(OrderResponse::new);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId) {
        return orderRepository.findById(orderId).map(OrderResponse::new).orElseThrow(OrderNotFoundException::new);
    }

    //UPDATE POST
    public void payOrder(Long orderId) {
        Order order = getOrderIfCreated(orderId);

        order.markAsPaid();
    }


    public void cancelOrder(Long orderId) {
        Order order = getOrderIfCreated(orderId);

        order.cancel();
    }
    //END UPDATE POST

    //HELPER
    private List<OrderItem> findOrderItems(CreateOrderRequest request) {
        return request.orderItems().stream().map(
                itemsRequest -> {
                    OrderItem item = new OrderItem();
                    Product product = productRepository.findById(itemsRequest.productId())
                            .orElseThrow(ProductNotFoundException::new);

                    item.setProduct(product);
                    item.setUnitPrice(product.getPrice());
                    item.setQuantity(itemsRequest.quantity());
                    return item;
                }
        ).toList();
    }

    private Order getOrderIfCreated(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        if (order.getStatus() != Status.CREATED)
            throw new InvalidOrderStatusException();

        return order;
    }
}
