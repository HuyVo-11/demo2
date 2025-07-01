package com.example.demo.controller;

import com.example.demo.dto.UserRequestDTO; // Import Request DTO
import com.example.demo.dto.UserResponseDTO; // Import Response DTO
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /**
     * CREATE: Tạo một người dùng mới từ UserRequestDTO.
     * POST /api/users
     *
     * @param requestDTO UserRequestDTO chứa thông tin từ client.
     * @return ResponseEntity chứa UserResponseDTO của người dùng đã tạo (HTTP 201 Created).
     */
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO requestDTO) {
        // 1. Chuyển đổi UserRequestDTO thành User Entity (chỉ các trường có trong DTO)
        User user = UserMapper.toEntity(requestDTO);

        // 2. Gán giá trị cho các trường NOT NULL hoặc cần giá trị mặc định/tạo tự động bởi BE
        user.setCreatedDate(LocalDateTime.now());
        user.setDeleted(false);
        user.setStatus("ACTIVE"); // Trạng thái mặc định
        user.setIsAdmin(false); // Quyền admin mặc định là false

        // Tạo code duy nhất ngẫu nhiên vì 'code' là NOT NULL và UNIQUE
        user.setCode("USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        // Gán firstName bằng lastName nếu không có trường firstName riêng trong UserRequestDTO
        // (hoặc bạn có thể yêu cầu client gửi cả firstName và lastName trong RequestDTO)
        user.setFirstName(requestDTO.getLastName());
        // Gán ngày sinh mặc định nếu không có trong RequestDTO
        user.setBirthday(LocalDate.of(2000, 1, 1));

        // 3. Lưu User Entity vào cơ sở dữ liệu
        User savedUser = userRepository.save(user);

        // 4. Chuyển đổi User Entity đã lưu thành UserResponseDTO để trả về client
        return new ResponseEntity<>(UserMapper.toResponseDTO(savedUser), HttpStatus.CREATED);
    }

    /**
     * READ: Lấy tất cả người dùng.
     * GET /api/users
     *
     * @return ResponseEntity chứa danh sách UserResponseDTO (HTTP 200 OK)
     * hoặc không có nội dung (HTTP 204 No Content) nếu danh sách rỗng.
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        // Map danh sách User Entity sang danh sách UserResponseDTO
        List<UserResponseDTO> responseDTOs = users.stream()
                .map(UserMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * READ: Lấy một người dùng theo ID.
     * GET /api/users/{id}
     *
     * @param id ID của người dùng.
     * @return ResponseEntity chứa UserResponseDTO của người dùng tìm thấy (HTTP 200 OK)
     * hoặc không tìm thấy (HTTP 404 Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(UserMapper::toResponseDTO) // Map User Entity sang UserResponseDTO
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * UPDATE: Cập nhật thông tin của một người dùng hiện có từ UserRequestDTO.
     * PUT /api/users/{id}
     *
     * @param id ID của người dùng cần cập nhật.
     * @param requestDTO UserRequestDTO chứa thông tin cập nhật từ client.
     * @return ResponseEntity chứa UserResponseDTO của người dùng đã cập nhật (HTTP 200 OK)
     * hoặc không tìm thấy (HTTP 404 Not Found).
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO requestDTO) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();

            // Cập nhật các trường từ requestDTO lên existingUser.
            // Các trường không có trong RequestDTO (như code, firstName, birthday, isAdmin, status, createdDate, deleted)
            // sẽ giữ nguyên giá trị hiện có của existingUser.
            existingUser.setAddress(requestDTO.getAddress());
            existingUser.setAvatar(requestDTO.getAvatar());
            existingUser.setDistrictId(requestDTO.getDistrictId());
            existingUser.setEmail(requestDTO.getEmail());

            if (requestDTO.getGender() != null) {
                existingUser.setGender(requestDTO.getGender() ? "Male" : "Female");
            } else {
                existingUser.setGender(null);
            }

            existingUser.setLastName(requestDTO.getLastName());
            existingUser.setPassword(requestDTO.getPassword()); // Cẩn thận: Mật khẩu nên được mã hóa và cập nhật qua service riêng
            existingUser.setPhone(requestDTO.getPhone());
            existingUser.setProvinceId(requestDTO.getProvinceId());
            existingUser.setWardId(requestDTO.getWardId());

            User updatedUser = userRepository.save(existingUser);
            return ResponseEntity.ok(UserMapper.toResponseDTO(updatedUser)); // Trả về UserResponseDTO đã cập nhật
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * DELETE (Soft Delete): Đánh dấu người dùng đã bị xóa.
     * DELETE /api/users/{id}
     *
     * @param id ID của người dùng cần xóa.
     * @return ResponseEntity không có nội dung (HTTP 204 No Content) nếu xóa thành công
     * hoặc không tìm thấy (HTTP 404 Not Found).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setDeleted(true); // Thực hiện soft delete
            userRepository.save(user);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
