package com.leandroftm.ordermanagement.order_management_api.repository;

import com.leandroftm.ordermanagement.order_management_api.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
