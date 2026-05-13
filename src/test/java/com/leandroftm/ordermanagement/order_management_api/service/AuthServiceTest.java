package com.leandroftm.ordermanagement.order_management_api.service;

import com.leandroftm.ordermanagement.order_management_api.security.dto.LoginRequest;
import com.leandroftm.ordermanagement.order_management_api.security.service.AuthService;
import com.leandroftm.ordermanagement.order_management_api.security.service.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager authManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldAuthenticateUserSuccessfully() {
        LoginRequest request = new LoginRequest("Email Test", "Password Test");

        Authentication auth = mock(Authentication.class);
        UserDetails user = mock(UserDetails.class);

        when(authManager.authenticate(any())).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(user);
        when(tokenService.generateToken(any())).thenReturn("jwt-token");

        String token = authService.authenticate(request);

        assertEquals("jwt-token", token);
        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).generateToken(user);
    }
}
