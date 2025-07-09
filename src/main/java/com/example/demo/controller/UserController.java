package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserFilterRequest;
import com.example.demo.dto.UserForm;
import com.example.demo.entity.User;
import com.example.demo.entity.UserStatus;
import com.example.demo.mapper.UserMapper;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.UserRepository;
import com.example.demo.payload.ApiResponse;

import com.example.demo.repository.UserRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import com.example.demo.entity.School;
import com.example.demo.repository.SchoolRepository;

import static java.util.Collection.*;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    @Qualifier("userRepositoryImpl")
    private UserRepositoryCustom userRepositoryCustom;

    @Autowired
    private UserMapper userMapper; //  dùng Spring bean
    private List<UserDto> users;

    //Create
    @PostMapping
    public ResponseEntity<ApiResponse<UserDto>> createUser(@RequestBody UserForm userForm) {
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
        if (userForm.getStatus() == null) {
            user.setStatus(UserStatus.ACTIVE);
        } else {
            try {
                user.setStatus(UserStatus.valueOf(userForm.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                ApiResponse<UserDto> errorResponse = new ApiResponse<>(false, "Status không hộ lệ", null);
                return ResponseEntity.badRequest().body(errorResponse);
            }
        }

        User savedUser = userRepository.save(user);
        UserDto userDto = userMapper.getModelFromEntity(savedUser);
        ApiResponse<UserDto> response = new ApiResponse<>(true, "Tạo thêm người dùng thành công", userDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    // List
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserDto>>> searchUsers(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long schoolId,
            @RequestParam(required = false) String dob) {

        // Tạo đối tượng UserFilterRequest
        UserFilterRequest request = new UserFilterRequest();
        request.setId(id);
        request.setName(name);
        request.setEmail(email);
        request.setStatus(status);
        request.setSchoolId(schoolId);

        // Xử lý dob
        if (dob != null && !dob.isBlank()) {
            try {
                LocalDate parsedDob = LocalDate.parse(dob, DateTimeFormatter.ISO_LOCAL_DATE);
                request.setDob(parsedDob);
            } catch (DateTimeParseException e) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Invalid date format for dob. Use yyyy-MM-dd", null));
            }
        }

        // Gọi repository để lọc người dùng
        List<User> users = userRepositoryCustom.filterUsers(request);

        // Chuyển đổi từ User sang UserDto
        List<UserDto> userDtos = users.stream()
                .map(user -> {
                    UserDto dto = userMapper.getModelFromEntity(user);
                    dto.setSchoolName(user.getSchool() != null ? user.getSchool().getName() : "");
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, "Danh sách người dùng đã lọc", userDtos));
    }

    // Detail
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDto dto = userMapper.getModelFromEntity(user);
            dto.setSchoolName(user.getSchool() != null ? user.getSchool().getName() : "");

            ApiResponse<UserDto> response = new ApiResponse<>(true, "Thông tin người dùng", dto);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<UserDto> errorResponse = new ApiResponse<>(false, "Không tìm thấy " + id, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }



    //Update
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@PathVariable Long id, @RequestBody UserForm userForm) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            userMapper.updateEntityFromModel(userForm, existingUser);

            //---gender---
            if (userForm.getGender() != null) {
                existingUser.setGender(userForm.getGender() ? "male" : "female");
            }

            //---school---
            if (userForm.getSchoolId() != null) {
                Optional<School> schoolOptional = schoolRepository.findById(userForm.getSchoolId());
                schoolOptional.ifPresent(existingUser::setSchool);
            }

            //---status---
            if (userForm.getStatus() != null) {
                try {
                    existingUser.setStatus(UserStatus.valueOf(userForm.getStatus().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    ApiResponse<UserDto> errorResponse = new ApiResponse<>(false, "Status không hợp lệ", null);
                    return ResponseEntity.badRequest().body(errorResponse);
                }
            } else if (existingUser.getStatus() == null) {
                existingUser.setStatus(UserStatus.ACTIVE);
            }

            User savedUser = userRepository.save(existingUser);
            UserDto userDto = userMapper.getModelFromEntity(savedUser);
            userDto.setSchoolName(savedUser.getSchool() != null ? savedUser.getSchool().getName() : "");

            ApiResponse<UserDto> response = new ApiResponse<>(true, "Cập nhật người dùng thành công", userDto);
            return ResponseEntity.ok(response);
        }

        ApiResponse<UserDto> notFoundResponse = new ApiResponse<>(false, "Không tìm thấy người dùng với ID: " + id, null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
    }


    //Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setDeleted(true);
            userRepository.save(user);

            ApiResponse<Void> response = new ApiResponse<>(true, "Xóa người dùng thành công", null);
            return ResponseEntity.ok(response);
        }

        ApiResponse<Void> notFoundResponse = new ApiResponse<>(false, "Không tim thấy người dùng: " + id, null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
    }

    @PostMapping("/filter")
    public ResponseEntity<ApiResponse<List<UserDto>>> filterUsers(@RequestBody UserFilterRequest request) {
        List<User> users = userRepositoryCustom.filterUsers(request);
        List<UserDto> userDtos = users.stream()
                .map(user -> {
                    UserDto dto = userMapper.getModelFromEntity(user);
                    dto.setSchoolName(user.getSchool() != null ? user.getSchool().getName() : "");
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, "Filtered users", userDtos));
    }

}














