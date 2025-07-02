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
    private UserMapper userMapper; //  dùng Spring bean

    //Create
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

    // List
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<UserDto> userDtos = users.stream()
                .map(user -> {
                    UserDto dto = userMapper.mapToUserDto(user);
                    dto.setSchoolName(user.getSchool() != null ? user.getSchool().getName() : "");
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    // Detail
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(u -> {
            UserDto dto = userMapper.mapToUserDto(u);
            dto.setSchoolName(u.getSchool() != null ? u.getSchool().getName() : "");
            return ResponseEntity.ok(dto);
        }).orElse(ResponseEntity.notFound().build());
    }


    //Update
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserForm userForm) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            User user = userMapper.mapToUser(userForm); //userform qua user
            existingUser.setAddress(user.getAddress());
            existingUser.setAvatar(user.getAvatar());
            existingUser.setEmail(user.getEmail());
            existingUser.setDistrictId(user.getDistrictId());
            existingUser.setLastName(user.getLastName());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setPassword(user.getPassword());
            existingUser.setPhone(user.getPhone());
            existingUser.setProvinceId(user.getProvinceId());
            existingUser.setWardId(user.getWardId());
            existingUser.setCode(user.getCode());
            existingUser.setCreatedDate(user.getCreatedDate());
            existingUser.setDeleted(user.isDeleted());
            existingUser.setIsAdmin(user.getIsAdmin());
            existingUser.setStatus(user.getStatus());
            existingUser.setBirthday(user.getBirthday());
            //gender
            if (userForm.getGender() != null) {
                existingUser.setGender(userForm.getGender() ? "male" : "female");
            }
            //school
            if (userForm.getSchool() != null && !userForm.getSchool().isEmpty()) {
                School school = schoolRepository.findByName(userForm.getSchool())
                        .orElseGet(() -> {
                            School newSchool = new School(null, userForm.getSchool());
                            return schoolRepository.save(newSchool);
                        });
                existingUser.setSchool(school);
            }
            User savedUser = userRepository.save(existingUser);
            UserDto userDto = userMapper.mapToUserDto(savedUser);
            userDto.setSchoolName(savedUser.getSchool() != null ? savedUser.getSchool().getName() : "");
            return ResponseEntity.ok(userDto);
        }
        return ResponseEntity.notFound().build();
    }


    //Delete
    @DeleteMapping("{id}")
    public  ResponseEntity<Void> deleteUser(@PathVariable Long id){
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            user.setDeleted(true);
            userRepository.save(user);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}














