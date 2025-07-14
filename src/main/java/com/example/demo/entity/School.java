package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Lớp Entity School, ánh xạ tới bảng 'school' trong cơ sở dữ liệu.
 * Sử dụng Lombok annotations để giảm thiểu boilerplate code.
 */
@Data // khỏi viết getter/setter thủ công, nhớ add lombok.Data
@NoArgsConstructor // tự động tạo một constructor đối số (default constructor)
@AllArgsConstructor // tự động tạo ra một constructor vơ tất các đối số, bao gồm cả các trường hợp kế thừa
@Entity // đánh dấu đây là lớp entity
@Table(name = "school") //bảng
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String name;
}
