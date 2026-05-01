package com.leandroftm.ordermanagement.order_management_api.security.service;

import com.leandroftm.ordermanagement.order_management_api.domain.entity.User;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.user.InvalidCredentialsException;
import com.leandroftm.ordermanagement.order_management_api.security.dto.LoginRequest;
import com.leandroftm.ordermanagement.order_management_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenService tokenService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public String authenticate(LoginRequest request) {
        User user = userService.findUserByEmail(request.email());
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }
        return tokenService.generateToken(user);
    }
}
