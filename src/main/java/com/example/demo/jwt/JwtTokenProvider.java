package com.example.demo.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Base64;

@Component
public class JwtTokenProvider {

    @Autowired
    private JwtProperties jwtProperties;

    private String encodedSecret;

    @PostConstruct
    public void init() {
        this.encodedSecret = Base64.getEncoder().encodeToString(jwtProperties.getSecret().getBytes());
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, encodedSecret)
                .compact();
    }
}
