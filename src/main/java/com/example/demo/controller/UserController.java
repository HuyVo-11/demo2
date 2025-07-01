package com.example.demo.controller;

import com.example.demo.dto.UserForm; // Import UserForm
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper; // Import UserMapper
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID; // Để tạo code ngẫu nhiên
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /**
     * CREATE: Tạo một người dùng mới từ UserForm.
     * POST /api/users
     *
     * @param userForm Đối tượng UserForm chứa thông tin cơ bản từ client.
     * @return ResponseEntity chứa UserForm của người dùng đã tạo (HTTP 201 Created).
     */
    @PostMapping
    public ResponseEntity<UserForm> createUser(@RequestBody UserForm userForm) {
        // 1. Chuyển đổi UserForm thành User Entity (chỉ các trường có trong form)
        User user = UserMapper.toEntity(userForm);

        // 2. Gán giá trị cho các trường NOT NULL hoặc cần giá trị mặc định/tạo tự động bởi BE
        user.setCreatedDate(LocalDateTime.now()); // Thời gian tạo
        user.setDeleted(false); // Mặc định không bị xóa (soft delete)
        user.setStatus("ACTIVE"); // Trạng thái mặc định
        user.setIsAdmin(false); // Quyền admin mặc định là false

        // Tạo code duy nhất ngẫu nhiên vì 'code' là NOT NULL và UNIQUE
        // Trong thực tế, bạn có thể có logic tạo code phức tạp hơn hoặc để client gửi lên (nếu client quản lý code)
        user.setCode("USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        // Gán firstName bằng lastName nếu không có trường firstName riêng trong UserForm
        // Hoặc bạn có thể yêu cầu client gửi cả firstName và lastName
        user.setFirstName(userForm.getLastName());
        // Gán ngày sinh mặc định nếu không có trong UserForm
        user.setBirthday(LocalDate.of(2000, 1, 1));

        // 3. Lưu User Entity vào cơ sở dữ liệu
        User savedUser = userRepository.save(user);

        // 4. Chuyển đổi User Entity đã lưu thành UserForm để trả về client
        return new ResponseEntity<>(UserMapper.toForm(savedUser), HttpStatus.CREATED); // 201 Created
    }

    /**
     * READ: Lấy tất cả người dùng.
     * GET /api/users
     *
     * @return ResponseEntity chứa danh sách UserForm (HTTP 200 OK)
     * hoặc không có nội dung (HTTP 204 No Content) nếu danh sách rỗng.
     */
    @GetMapping
    public ResponseEntity<List<UserForm>> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        // Map danh sách User Entity sang danh sách UserForm DTO
        List<UserForm> userForms = users.stream()
                .map(UserMapper::toForm)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userForms); // 200 OK
    }

    /**
     * READ: Lấy một người dùng theo ID.
     * GET /api/users/{id}
     *
     * @param id ID của người dùng.
     * @return ResponseEntity chứa UserForm của người dùng tìm thấy (HTTP 200 OK)
     * hoặc không tìm thấy (HTTP 404 Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserForm> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(UserMapper::toForm) // Map User Entity sang UserForm DTO
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // 404 Not Found
    }

    /**
     * UPDATE: Cập nhật thông tin của một người dùng hiện có từ UserForm.
     * PUT /api/users/{id}
     *
     * @param id ID của người dùng cần cập nhật.
     * @param userForm Đối tượng UserForm chứa thông tin cập nhật từ client.
     * @return ResponseEntity chứa UserForm của người dùng đã cập nhật (HTTP 200 OK)
     * hoặc không tìm thấy (HTTP 404 Not Found).
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserForm> updateUser(@PathVariable Long id, @RequestBody UserForm userForm) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();

            // Cập nhật các trường từ userForm lên existingUser.
            // Các trường không có trong UserForm (như code, firstName, birthday, isAdmin, status, createdDate, deleted)
            // sẽ giữ nguyên giá trị hiện có của existingUser.
            existingUser.setAddress(userForm.getAddress());
            existingUser.setAvatar(userForm.getAvatar());
            existingUser.setDistrictId(userForm.getDistrictId());
            existingUser.setEmail(userForm.getEmail());

            if (userForm.getGender() != null) {
                existingUser.setGender(userForm.getGender() ? "Male" : "Female");
            } else {
                existingUser.setGender(null);
            }

            existingUser.setLastName(userForm.getLastName());
            existingUser.setPassword(userForm.getPassword()); // Cẩn thận: Mật khẩu nên được mã hóa và cập nhật qua service riêng
            existingUser.setPhone(userForm.getPhone());
            existingUser.setProvinceId(userForm.getProvinceId());
            existingUser.setWardId(userForm.getWardId());

            User updatedUser = userRepository.save(existingUser);
            return ResponseEntity.ok(UserMapper.toForm(updatedUser)); // Trả về UserForm đã cập nhật
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
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.notFound().build();
    }
}
