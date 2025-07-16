package com.example.demo.security;

import com.example.demo.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class UserDetailsImpl implements UserDetails {

    private Long id;
    private String code;
    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl build(User user) {
        // Nếu bạn có role, bạn có thể map vào authorities ở đây
        return new UserDetailsImpl(
                user.getId(),
                user.getCode(),
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>() // hoặc List.of(new SimpleGrantedAuthority("ROLE_USER")) nếu cần phân quyền
        );
    }

    @Override
    public String getUsername() {
        return code; // dùng code làm username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
