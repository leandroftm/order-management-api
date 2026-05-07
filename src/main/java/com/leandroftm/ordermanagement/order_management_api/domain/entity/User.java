package com.leandroftm.ordermanagement.order_management_api.domain.entity;

import com.leandroftm.ordermanagement.order_management_api.domain.enums.Role;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.user.InvalidRoleTransitionException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.user.UserAlreadyActiveException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.user.UserAlreadyInactiveException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private boolean enabled;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;
    }

    public User(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = true;
    }

    public static User create(String email, String password, Role role) {
        return new User(email, password, role);
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDetails(String email, String password) {
        if (email != null)
            this.email = email;
        if (password != null)
            this.password = password;
    }

    public void enable() {
        if (this.enabled) {
            throw new UserAlreadyActiveException();
        }
        this.enabled = true;
    }

    //REMOVED assign user role
//    public void updateRole(Role role) {
//        if (role == getRole()) {
//            throw new InvalidUserRoleException(); // same role assigned
//        }
//        this.role = role;
//    }

    public void disable() {
        if (!this.enabled) {
            throw new UserAlreadyInactiveException();
        }
        if(this.role == Role.ADMIN) {
            throw new InvalidRoleTransitionException();
        }
        this.enabled = false;
    }
}