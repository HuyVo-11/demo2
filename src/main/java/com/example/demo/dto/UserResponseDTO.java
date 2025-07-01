package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String address;
    private String avatar;
    private LocalDate birthday;
    private String code;
    private LocalDateTime createdDate;
    private Boolean deleted; // Trả về trạng thái xóa
    private Integer districtId;
    private String email;
    private String firstName;
    private String gender; // String "Male"/"Female"
    private Boolean isAdmin; // Trả về quyền admin
    private String lastName;
    // KHÔNG có password ở đây!
    private String phone;
    private Integer provinceId;
    private String status;
    private Integer wardId;
}
