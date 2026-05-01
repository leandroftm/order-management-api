package com.leandroftm.ordermanagement.order_management_api.domain.entity;

import com.leandroftm.ordermanagement.order_management_api.domain.enums.Status;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.order.InvalidOrderStatusException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.order.OrderItemsListIsEmptyException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.order.InvalidUserException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();


    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    private Order(List<OrderItem> orderItems) {
        this.status = Status.CREATED;

        for (OrderItem item : orderItems) {
            item.getProduct().decreaseStock(item.getQuantity());
            addOrderItem(item);
        }
    }

    public static Order create(User user, List<OrderItem> orderItems) {
        if(user == null) {
            throw new InvalidUserException();
        }
        if(orderItems == null || orderItems.isEmpty()) {
            throw new OrderItemsListIsEmptyException();
        }
        Order order = new Order(orderItems);
        order.user =  user;
        return order;
    }

    public void cancel() {
        if (this.status != Status.CREATED) {
            throw new InvalidOrderStatusException();
        }

        this.status = Status.CANCELLED;

        for (OrderItem item : this.orderItems) {
            item.restoreStock();
        }
    }

    public void markAsPaid() {
        if (this.status != Status.CREATED) {
            throw new InvalidOrderStatusException();
        }
        this.status = Status.PAID;
    }


    private void recalculateTotal() {
        this.totalAmount = orderItems.stream()
                .map(OrderItem::calculateItemTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void addOrderItem(OrderItem orderItem) {
        orderItem.setOrder(this);
        orderItems.add(orderItem);
        recalculateTotal();
    }

    //not used for now
    /*
    private void removeOrderItem(OrderItem orderItem) {
        BigDecimal itemTotal = orderItem.getUnitPrice()
                .multiply(BigDecimal.valueOf(orderItem.getQuantity()));

        this.totalAmount = this.totalAmount.subtract(itemTotal);
        orderItem.setOrder(null);
        orderItems.remove(orderItem);
    }
    */
}
