package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String code) throws UsernameNotFoundException {
        User user = userRepository.findByCode(code)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với code: " + code));

        // Nếu bạn dùng role là isAdmin
        String role = user.getIsAdmin() != null && user.getIsAdmin() ? "ROLE_ADMIN" : "ROLE_USER";

        return UserDetailsImpl.build(user);
    }
}
