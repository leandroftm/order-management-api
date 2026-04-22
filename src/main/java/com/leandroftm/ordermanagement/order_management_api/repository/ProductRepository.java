package com.leandroftm.ordermanagement.order_management_api.repository;

import com.leandroftm.ordermanagement.order_management_api.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
