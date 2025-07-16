package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.TokenRefreshRequest;
import com.example.demo.dto.TokenRefreshResponse;
import com.example.demo.entity.RefreshToken;
import com.example.demo.exception.TokenRefreshException;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.RefreshTokenService;
import com.example.demo.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired AuthenticationManager authenticationManager;
    @Autowired  JwtTokenProvider jwtTokenProvider;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtTokenProvider.generateTokenFromUsername(userDetails.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new LoginResponse(jwt, refreshToken.getToken()));
    }


    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        String requestToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtTokenProvider.generateTokenFromUsername(user.getCode());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestToken, "Refresh token is not in DB!"));
    }

}

