package com.example.demo.repository;

import com.example.demo.entity.User;
import com.example.demo.dto.UserFilterRequest;

import java.util.List;

public interface UserRepositoryCustom {
    List<User> filterUsers(UserFilterRequest request);
}
