package com.example.demo.dto;

import lombok.Data;

@Data
public class UserForm {
    private String address;
    private String avatar;
    private Integer districtId;
    private String email;
    private Boolean gender;
    private String lastName;
    private String password;
    private String phone;
    private Integer provinceId;
    private Integer wardId;
    private Long schoolId;
    private String birthday;
    private String status;
}