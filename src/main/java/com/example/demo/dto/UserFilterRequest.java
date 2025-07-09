package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UserFilterRequest {
    private Long id;
    private String name;
    private String email;
    private String status;
    private Long schoolId;
    private LocalDate dob;



}





