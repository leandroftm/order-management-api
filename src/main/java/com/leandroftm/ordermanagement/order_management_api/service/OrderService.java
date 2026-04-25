package com.leandroftm.ordermanagement.order_management_api.service;

import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.create.CreateOrderRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.response.OrderResponse;
import com.leandroftm.ordermanagement.order_management_api.domain.entity.Order;
import com.leandroftm.ordermanagement.order_management_api.domain.entity.OrderItem;
import com.leandroftm.ordermanagement.order_management_api.domain.entity.Product;
import com.leandroftm.ordermanagement.order_management_api.domain.entity.User;
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

    public OrderResponse createOrder(Long userId, CreateOrderRequest request) {
        //fill order items with products
        List<OrderItem> items = findOrderItems(request);

        for (OrderItem item : items) {
            if (item.getProduct().getStock() < item.getQuantity()) {
                throw new InsuficientStockException();
            }
        }

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Order order = new Order();
        order.create(user);

        for (OrderItem item : items) {
            item.getProduct().decreaseStock(item.getQuantity());
            order.addOrderItem(item);
        }

        Order savedOrder = orderRepository.save(order);
        return new OrderResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(OrderResponse::new);
    }

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
}
