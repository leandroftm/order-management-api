package com.leandroftm.ordermanagement.order_management_api.repository;

import com.leandroftm.ordermanagement.order_management_api.domain.dto.response.ProductResponse;
import com.leandroftm.ordermanagement.order_management_api.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<ProductResponse> findAllByCategory(Long categoryId, Pageable pageable);

    Optional<Product> findByIdAndCategoryId(Long productId, Long categoryId);

    boolean existsByCategoryId(Long categoryId);
}
