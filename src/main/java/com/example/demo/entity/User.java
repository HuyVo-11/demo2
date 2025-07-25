package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data // khỏi viết getter/setter thủ công, nhớ add lombok.Data
@NoArgsConstructor // tự động tạo một constructor đối số (default constructor)
@AllArgsConstructor // tự động tạo ra một constructor vơ tất các đối số, bao gồm cả các trường hợp kế thừa
@Entity // đánh dấu đây là lớp entity
@Table(name = "user") // tạo bảng (name = "")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255 ) // tạo các cột trong bảng, TRONG () là mô tả của cột
    private String address;

    @Column(length = 255)
    private String avatar;

    private LocalDate birthday;

    @Column(nullable = false, length = 50, unique = true)
    private String code;

    private LocalDateTime createdDate;

    @Column(nullable = false)
    private boolean deleted = false;

    private Integer districtId;

    @Column(length = 255)
    private String email;

    @Column(name = "first_name", length = 45)
    private String firstName;

    @Column(length = 10)
    private String gender; // trong DB là String, trong Form là Boolean.

    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin = true; // trong DB là boolean, trong Form là Boolean.

    @Column(name = "last_name", length = 45)
    private String lastName;

    @Column(length = 255)
    private String password;

    @Column(length = 10)
    private String phone;

    private Integer provinceId;

    @Column(nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private Integer wardId;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;
}