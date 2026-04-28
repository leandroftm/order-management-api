package com.leandroftm.ordermanagement.order_management_api.domain.entity;

import com.leandroftm.ordermanagement.order_management_api.domain.enums.Status;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.order.InvalidOrderStatusException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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
        this.status = Status.CREATED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public void create(User user, List<OrderItem> orderItems) {
        this.user = user;

        for (OrderItem item : orderItems) {
            item.getProduct().decreaseStock(item.getQuantity());
            addOrderItem(item);
        }
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

    public void addOrderItem(OrderItem orderItem) {
        orderItem.setOrder(this);
        orderItems.add(orderItem);
        recalculateTotal();
    }

    public void removeOrderItem(OrderItem orderItem) {
        BigDecimal itemTotal = orderItem.getUnitPrice()
                .multiply(BigDecimal.valueOf(orderItem.getQuantity()));

        this.totalAmount = this.totalAmount.subtract(totalAmount.add(itemTotal));
        orderItem.setOrder(null);
        orderItems.remove(orderItem);
    }
}
