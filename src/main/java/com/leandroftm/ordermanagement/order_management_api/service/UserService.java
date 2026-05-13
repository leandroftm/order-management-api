package com.leandroftm.ordermanagement.order_management_api.service;

import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.create.CreateUserRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.update.UpdateUserRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.response.UserResponse;
import com.leandroftm.ordermanagement.order_management_api.domain.entity.User;
import com.leandroftm.ordermanagement.order_management_api.domain.enums.Role;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.user.*;
import com.leandroftm.ordermanagement.order_management_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long create(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException();
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = User.create(request.email(), encodedPassword, request.role());

        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getUsers(Role role, Pageable pageable) {
        if (role != null)
            return userRepository.findByRoleIgnoreCase(role, pageable).map(UserResponse::new);
        return userRepository.findAll(pageable).map(UserResponse::new);
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(Long id) {
        return userRepository.findById(id).map(UserResponse::new)
                .orElseThrow(UserNotFoundException::new);
    }

    //get users by role merged with get all users

    //get user by email moved to CustomUserDetailsService

    public void update(Long id, UpdateUserRequest request) {
        if (request.email() == null && request.password() == null) {
            return;
        }

        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        if (request.email() != null) {
            validateEmail(user.getEmail(), request.email());
        }
        user.updateDetails(request.email(), passwordEncoder.encode(request.password()));
        userRepository.save(user);
    }

    public void disable(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.disable();
        userRepository.save(user);
    }

    public void enable(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.enable();
        userRepository.save(user);
    }

    //REMOVED assign user role
//    public void assignRole(Long id, UpdateUserRoleRequest request) {
//        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
//        user.updateRole(request.role());
//    }

    private void validateEmail(String oldEmail, String newEmail) {
        if (oldEmail.equals(newEmail)) {
            throw new InvalidEmailException();
        } else if (userRepository.existsByEmail(newEmail)) {
            throw new EmailAlreadyExistsException();
        }
    }
}
