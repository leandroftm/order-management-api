package com.leandroftm.ordermanagement.order_management_api.repository;

import com.leandroftm.ordermanagement.order_management_api.domain.entity.Order;
import com.leandroftm.ordermanagement.order_management_api.domain.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Page<Order> findByUserId(Long userId, Pageable pageable);

    Page<Order> findByUserIdAndStatus(Long userId, Status status, Pageable pageable);

    Page<Order> findByStatus(Status status, Pageable pageable);
}
