package com.leandroftm.ordermanagement.order_management_api.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final String SECRET = "my-secret-key";

    public String generateToken(UserDetails user) {
      return Jwts.builder
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
