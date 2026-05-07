package com.leandroftm.ordermanagement.order_management_api.security.service;

import com.leandroftm.ordermanagement.order_management_api.domain.entity.User;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.user.UserNotFoundException;
import com.leandroftm.ordermanagement.order_management_api.repository.UserRepository;
import com.leandroftm.ordermanagement.order_management_api.security.entity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(UserNotFoundException::new);

        return new UserPrincipal(user);
    }
}
