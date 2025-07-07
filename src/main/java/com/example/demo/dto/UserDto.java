package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;
    private String address; //
    private String avatar; //
    private LocalDate birthday; //
    private String code; //
    private LocalDateTime createdDate;
    private Boolean deleted;
    private Integer districtId; //
    private String email; //
    private String firstName; //
    private String gender; //
    private Boolean isAdmin;
    private String lastName; //
    private String password;
    private String phone; //
    private Integer provinceId; //
    private String status;
    private Integer wardId; //
    private String schoolName; //
}