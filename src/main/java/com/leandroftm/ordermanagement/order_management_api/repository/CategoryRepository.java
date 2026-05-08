package com.leandroftm.ordermanagement.order_management_api.repository;

import com.leandroftm.ordermanagement.order_management_api.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    boolean existsByNameIgnoreCase(String name);
}
