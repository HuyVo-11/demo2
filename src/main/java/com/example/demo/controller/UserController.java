package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserForm;
import com.example.demo.dto.UserFilterRequest;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    //Create
    @PostMapping
    public ResponseEntity<ApiResponse<UserDto>> createUser(@RequestBody UserForm userForm) {
        ApiResponse<UserDto> response = userService.createUser(userForm);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    //List
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<UserDto>>> searchUsers(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long schoolId) {

        UserFilterRequest request = new UserFilterRequest();
        request.setId(id);
        request.setName(name);
        request.setStatus(status);
        request.setSchoolId(schoolId);

        ApiResponse<List<UserDto>> response = userService.filterUsers(request);
        return ResponseEntity.ok(response);
    }

    //Details
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long id) {
        ApiResponse<UserDto> response = userService.getUserById(id);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    //Update
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@PathVariable Long id, @RequestBody UserForm userForm) {
        ApiResponse<UserDto> response = userService.updateUser(id, userForm);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    //Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        ApiResponse<Void> response = userService.deleteUser(id);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

}