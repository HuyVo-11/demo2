package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserForm;
import com.example.demo.dto.UserFilterRequest;
import com.example.demo.entity.School;
import com.example.demo.entity.User;
import com.example.demo.entity.UserStatus;
import com.example.demo.mapper.UserMapper;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.SchoolRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Qualifier("userRepositoryImpl")
    private UserRepositoryCustom userRepositoryCustom;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public ApiResponse<UserDto> createUser(UserForm userForm) {
        User user = userMapper.getEntityFromModel(userForm);
        user.setCreatedDate(LocalDateTime.now());
        user.setDeleted(false);
        user.setIsAdmin(false);
        user.setCode("USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        if (userForm.getGender() != null) {
            user.setGender(userForm.getGender() ? "Male" : "Female");
        }

        if (userForm.getSchoolId() != null) {
            Optional<School> schoolOptional = schoolRepository.findById(userForm.getSchoolId());
            if (schoolOptional.isPresent()) {
                user.setSchool(schoolOptional.get());
            } else {
                return new ApiResponse<>(false, "Không tìm thấy trường với ID: " + userForm.getSchoolId(), null);
            }
        }

        if (userForm.getStatus() == null) {
            user.setStatus(UserStatus.ACTIVE);
        } else {
            try {
                user.setStatus(UserStatus.valueOf(userForm.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                return new ApiResponse<>(false, "Status không hợp lệ", null);
            }
        }

        User savedUser = userRepository.save(user);
        UserDto userDto = userMapper.getModelFromEntity(savedUser);
        return new ApiResponse<>(true, "Tạo thêm người dùng thành công", userDto);
    }

    @Override
    public ApiResponse<List<UserDto>> filterUsers(UserFilterRequest request) {
        List<User> users = userRepositoryCustom.filterUsers(request);
        List<UserDto> userDtos = users.stream()
                .map(user -> {
                    UserDto dto = userMapper.getModelFromEntity(user);
                    dto.setSchoolName(user.getSchool() != null ? user.getSchool().getName() : "");
                    return dto;
                })
                .collect(Collectors.toList());
        return new ApiResponse<>(true, "Danh sách người dùng đã lọc", userDtos);
    }

    @Override
    public ApiResponse<UserDto> getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDto dto = userMapper.getModelFromEntity(user);
            dto.setSchoolName(user.getSchool() != null ? user.getSchool().getName() : "");
            return new ApiResponse<>(true, "Thông tin người dùng", dto);
        }
        return new ApiResponse<>(false, "Không tìm thấy người dùng với ID: " + id, null);
    }

    @Override
    public ApiResponse<UserDto> updateUser(Long id, UserForm userForm) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            userMapper.updateEntityFromModel(userForm, existingUser);

            if (userForm.getGender() != null) {
                existingUser.setGender(userForm.getGender() ? "Male" : "Female");
            }

            if (userForm.getSchoolId() != null) {
                Optional<School> schoolOptional = schoolRepository.findById(userForm.getSchoolId());
                if (schoolOptional.isPresent()) {
                    existingUser.setSchool(schoolOptional.get());
                } else {
                    return new ApiResponse<>(false, "Không tìm thấy trường với ID: " + userForm.getSchoolId(), null);
                }
            }

            if (userForm.getStatus() != null) {
                try {
                    existingUser.setStatus(UserStatus.valueOf(userForm.getStatus().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    return new ApiResponse<>(false, "Status không hợp lệ", null);
                }
            } else if (existingUser.getStatus() == null) {
                existingUser.setStatus(UserStatus.ACTIVE);
            }

            User savedUser = userRepository.save(existingUser);
            UserDto userDto = userMapper.getModelFromEntity(savedUser);
            userDto.setSchoolName(savedUser.getSchool() != null ? savedUser.getSchool().getName() : "");
            return new ApiResponse<>(true, "Cập nhật người dùng thành công", userDto);
        }
        return new ApiResponse<>(false, "Không tìm thấy người dùng với ID: " + id, null);
    }

    @Override
    public ApiResponse<Void> deleteUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setDeleted(true);
            userRepository.save(user);
            return new ApiResponse<>(true, "Xóa người dùng thành công", null);
        }
        return new ApiResponse<>(false, "Không tìm thấy người dùng với ID: " + id, null);
    }
}