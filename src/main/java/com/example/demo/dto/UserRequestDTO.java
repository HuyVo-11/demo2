package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/*
 * DTO đầu vào (Request DTO) cho User.
 * Chứa các trường mà client gửi lên khi tạo hoặc cập nhật User.
 * KHÔNG bao gồm các trường do backend tự quản lý (id, createdDate, deleted, code, isAdmin, status).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    private String address;
    private String avatar;
    private Integer districtId;
    private String email;
    private Boolean gender; // true cho Male, false cho Female, null nếu không xác định
    private String lastName;
    private String password;
    private String phone;
    private Integer provinceId;
    private Integer wardId;
    private Boolean isAdmin;
    // Không có các trường như id, code, firstName, birthday, createdDate, deleted, isAdmin, status
    // vì chúng sẽ được backend tự động tạo/quản lý.
}
