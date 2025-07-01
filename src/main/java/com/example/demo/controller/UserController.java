package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserForm;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.demo.entity.School;
import com.example.demo.repository.SchoolRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private UserMapper userMapper; // ✅ dùng Spring bean

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserForm userForm) {
        User user = userMapper.mapToUser(userForm);
        user.setCreatedDate(LocalDateTime.now());
        user.setDeleted(false);
        user.setStatus("ACTIVE");
        user.setIsAdmin(false);
        user.setCode("USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        user.setFirstName(userForm.getLastName());
        user.setBirthday(LocalDate.of(2000, 1, 1));

        if (userForm.getGender() != null) {
            user.setGender(userForm.getGender() ? "Male" : "Female");
        }

        if (userForm.getSchool() != null && !userForm.getSchool().isEmpty()) {
            School school = schoolRepository.findByName(userForm.getSchool())
                    .orElseGet(() -> schoolRepository.save(new School(null, userForm.getSchool())));
            user.setSchool(school);
        }

        User savedUser = userRepository.save(user);
        UserDto userDto = userMapper.mapToUserDto(savedUser);
        userDto.setSchoolName(user.getSchool() != null ? user.getSchool().getName() : null);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }
}
