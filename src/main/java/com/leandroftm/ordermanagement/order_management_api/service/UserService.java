package com.leandroftm.ordermanagement.order_management_api.service;

import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.create.CreateUserRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.update.UpdateUserRoleRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.response.UserResponse;
import com.leandroftm.ordermanagement.order_management_api.domain.entity.User;
import com.leandroftm.ordermanagement.order_management_api.domain.enums.Role;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.user.*;
import com.leandroftm.ordermanagement.order_management_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Long create(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException();
        }

        User user = User.create(request.email(), request.password(), request.role());

        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserResponse::new);
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(Long id) {
        return userRepository.findById(id).map(UserResponse::new)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getUsersByRole(Role role, Pageable pageable) {
        return userRepository.findByRoleIgnoreCase(role, pageable).map(UserResponse::new);
    }

    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).orElseThrow(InvalidCredentialsException::new);
    }

    public void update(Long id, String email, String password) {
        if (email == null && password == null) {
            return;
        }

        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        if (email != null) {
            validateEmail(user.getEmail(), email);
        }
        user.updateDetails(email, password);
    }

    public void disable(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.disable();
    }

    public void enable(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.enable();
    }

    public void assignRole(Long id, UpdateUserRoleRequest request) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.updateRole(request.role());
    }

    private void validateEmail(String oldEmail, String newEmail) {
        if (oldEmail.equals(newEmail)) {
            throw new InvalidEmailException();
        } else if (userRepository.existsByEmail(newEmail)) {
            throw new EmailAlreadyExistsException();
        }
    }
}
