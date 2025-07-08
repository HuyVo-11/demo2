package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserForm;
import com.example.demo.entity.User;
import com.example.demo.entity.UserStatus;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;

import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        User user = userMapper.getEntityFromModel(userForm);
        user.setCreatedDate(LocalDateTime.now());
        user.setDeleted(false);
        user.setIsAdmin(false);
        user.setCode("USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        //---gender---
        if (userForm.getGender() != null) {
            user.setGender(userForm.getGender() ? "Male" : "Female");
        }

        //---schoolId---
        if (userForm.getSchoolId() != null) {
            Optional<School> schoolOptional = schoolRepository.findById(userForm.getSchoolId());
            schoolOptional.ifPresent(user::setSchool);
        }

        // ---Status---
        if (userForm.getStatus() == null){
            user.setStatus(UserStatus.ACTIVE);
        } else {
            try {
                user.setStatus(UserStatus.valueOf(userForm.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e){
                return ResponseEntity.badRequest().body(null);
            }
        }


        User savedUser = userRepository.save(user);
        UserDto userDto = userMapper.getModelFromEntity(savedUser);
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
                .map(user -> userMapper.getModelFromEntity(user))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    // Detail
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDto dto = userMapper.getModelFromEntity(user);
            dto.setSchoolName(user.getSchool() != null ? user.getSchool().getName() : "");
            return dto;
        } else {
            return null;
        }
    }


    //Update
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserForm userForm) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            userMapper.updateEntityFromModel(userForm, existingUser);

            //---gender---
            if (userForm.getGender() != null) {
                existingUser.setGender(userForm.getGender() ? "male" : "female");
            }

            //---school---
            if (userForm.getSchoolId() !=null) {
                Optional<School> schoolOptional = schoolRepository.findById(userForm.getSchoolId());
                schoolOptional.ifPresent(existingUser::setSchool);
            }

            //---status---
            if(userForm.getStatus() !=null){
                try {
                    existingUser.setStatus(UserStatus.valueOf(userForm.getStatus().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body(null);
                }
            } else {
                if (existingUser.getStatus() == null){
                    existingUser.setStatus(UserStatus.ACTIVE);
                }
            }

            User savedUser = userRepository.save(existingUser);
            UserDto userDto = userMapper.getModelFromEntity(savedUser);
            userDto.setSchoolName(savedUser.getSchool() != null ? savedUser.getSchool().getName() : "");
            return ResponseEntity.ok(userDto);
        }
        return ResponseEntity.notFound().build();
    }


    //Delete
    @DeleteMapping("/{id}")
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














