package com.leandroftm.ordermanagement.order_management_api.repository;

import com.leandroftm.ordermanagement.order_management_api.domain.entity.User;
import com.leandroftm.ordermanagement.order_management_api.domain.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Page<User> findByRoleIgnoreCase(Role role, Pageable pageable);

    Optional<User> findByEmailIgnoreCase(String email);
}
