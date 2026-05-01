package com.leandroftm.ordermanagement.order_management_api.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.leandroftm.ordermanagement.order_management_api.domain.entity.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {
    private final String secret = "my-secret-key";

    public String generateToken(User user) {
       return JWT.create()
               .withSubject(user.getId().toString())
               .withExpiresAt(Instant.now().plus(2, ChronoUnit.HOURS))
               .sign(Algorithm.HMAC256(secret));
    }

    public String validateToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token)
                    .getSubject(); //return userId
        }catch (JWTVerificationException e){
            return null;
        }
    }
}
