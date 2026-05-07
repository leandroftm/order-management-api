package com.leandroftm.ordermanagement.order_management_api.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {
    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(UserDetails user) {
       return JWT.create()
               .withSubject(user.getUsername())
               .withExpiresAt(Instant.now().plus(2, ChronoUnit.HOURS))
               .sign(Algorithm.HMAC256(secret));
    }

    public String extractUsername(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token)
                    .getSubject(); //return user email
        }catch (JWTVerificationException e){
            return null;
        }
    }
}
