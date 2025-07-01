package com.example.demo.mapper;

import com.example.demo.dto.UserRequestDTO; // Import Request DTO
import com.example.demo.dto.UserResponseDTO; // Import Response DTO
import com.example.demo.entity.User;

public class UserMapper {

    
     //Chuyển đổi từ UserRequestDTO (đầu vào từ client) sang User Entity.

    public static User toEntity(UserRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        User user = new User();
        user.setAddress(requestDTO.getAddress());
        user.setAvatar(requestDTO.getAvatar());
        user.setDistrictId(requestDTO.getDistrictId());
        user.setEmail(requestDTO.getEmail());
        // Chuyển đổi Boolean gender từ RequestDTO sang String gender trong User Entity
        if (requestDTO.getGender() != null) {
            user.setGender(requestDTO.getGender() ? "Male" : "Female");
        } else {
            user.setGender(null);
        }
        user.setLastName(requestDTO.getLastName());
        user.setPassword(requestDTO.getPassword()); // Mật khẩu sẽ được mã hóa sau đó
        user.setPhone(requestDTO.getPhone());
        user.setProvinceId(requestDTO.getProvinceId());
        user.setWardId(requestDTO.getWardId());

        return user;
    }

    public static UserResponseDTO toResponseDTO(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setAddress(user.getAddress());
        responseDTO.setAvatar(user.getAvatar());
        responseDTO.setBirthday(user.getBirthday());
        responseDTO.setCode(user.getCode());
        responseDTO.setCreatedDate(user.getCreatedDate());
        responseDTO.setDeleted(user.isDeleted());
        responseDTO.setDistrictId(user.getDistrictId());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setFirstName(user.getFirstName());
        responseDTO.setGender(user.getGender());
        responseDTO.setIsAdmin(user.getIsAdmin());
        responseDTO.setLastName(user.getLastName());
        responseDTO.setPhone(user.getPhone());
        responseDTO.setProvinceId(user.getProvinceId());
        responseDTO.setStatus(user.getStatus());
        responseDTO.setWardId(user.getWardId());
        return responseDTO;
    }
}
