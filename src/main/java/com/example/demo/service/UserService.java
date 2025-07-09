package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserForm;
import com.example.demo.dto.UserFilterRequest;
import com.example.demo.payload.ApiResponse;

import java.util.List;

public interface UserService {
    ApiResponse<UserDto> createUser(UserForm userForm);
    ApiResponse<List<UserDto>> filterUsers(UserFilterRequest request);
    ApiResponse<UserDto> getUserById(Long id);
    ApiResponse<UserDto> updateUser(Long id, UserForm userForm);
    ApiResponse<Void> deleteUser(Long id);
}