package com.example.demo; // Đảm bảo package này khớp với package của bạn

import com.example.demo.entity.User;
import com.example.demo.entity.UserStatus;
import com.example.demo.repository.SchoolRepository; // Import các Repository
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner; // Import CommandLineRunner
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean; // Import @Bean
import com.example.demo.entity.School;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@ComponentScan
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	/**
	 * Định nghĩa một Bean CommandLineRunner. Spring sẽ tự động gọi phương thức này
	 * sau khi ứng dụng khởi động và tất cả các Bean khác đã được khởi tạo.
	 * Spring sẽ tự động inject UserRepository và SchoolRepository vào phương thức này.
	 *
	 * @param userRepository Bean UserRepository được inject bởi Spring.
	 * @param schoolRepository Bean SchoolRepository được inject bởi Spring.
	 * @return Một instance của CommandLineRunner.
	 */
	@Bean
	public CommandLineRunner demoData(UserRepository userRepository, SchoolRepository schoolRepository, PasswordEncoder passwordEncoder) {
		// Bên trong phương thức này, bạn có thể gọi các phương thức non-static của repository.
		return args -> {
			System.out.println("--- Đang khởi tạo dữ liệu demo ---");

			// 1. Tạo và lưu một số School
			School schoolA = new School(null, "Hoa Sen");
			School schoolB = new School(null, "Tôn Đức Thắng");
			schoolRepository.save(schoolA); // Sử dụng repository đã được inject
			schoolRepository.save(schoolB);
			System.out.println("Đã lưu School A: " + schoolA.getName());
			System.out.println("Đã lưu School B: " + schoolB.getName());

			// 2. Tạo và lưu một số User(Create)
			User user1 = new User();
			user1.setCode("USER1");
			user1.setFirstName("Johnny");
			user1.setLastName("Dang");
			user1.setEmail("JohnnyDang@gmail.com");
			user1.setPhone("0901111111");
			user1.setBirthday(LocalDate.of(1995, 10, 20));
			user1.setCreatedDate(LocalDateTime.now());
			user1.setGender("Nam");
			user1.setStatus(UserStatus.valueOf("ACTIVE"));
			user1.setIsAdmin(false);
			user1.setDeleted(false);
			user1.setPassword(passwordEncoder.encode("JD11111"));
			user1.setSchool(schoolA); // Gán User này cho School A
			userRepository.save(user1); // Sử dụng repository đã được inject
			System.out.println("Đã lưu User 1: " + user1.getFirstName() + " " + user1.getLastName());

			User user2 = new User();
			user2.setCode("USER2");
			user2.setFirstName("Khoa");
			user2.setLastName("Pug");
			user2.setEmail("KhoaPub@gmail.com");
			user2.setPhone("0902222222");
			user2.setBirthday(LocalDate.of(1998, 5, 15));
			user2.setCreatedDate(LocalDateTime.now());
			user2.setGender("Nam");
			user2.setStatus(UserStatus.valueOf("INACTIVE"));
			user2.setIsAdmin(false);
			user2.setDeleted(false);
			user2.setPassword(passwordEncoder.encode("KP222222"));
			user2.setSchool(schoolB); // Gán User này cho School B
			userRepository.save(user2);

			System.out.println("Đã lưu User 2: " + user2.getFirstName() + " " + user2.getLastName());

			User adminUser = new User();
			adminUser.setCode("ADMIN1");
			adminUser.setFirstName("NoBi");
			adminUser.setLastName("Ta");
			adminUser.setEmail("NoBiTa@gmail.com");
			adminUser.setPhone("0903333333");
			adminUser.setBirthday(LocalDate.of(2000, 1, 1));
			adminUser.setCreatedDate(LocalDateTime.now());
			adminUser.setGender("Other");
			adminUser.setStatus(UserStatus.valueOf("ACTIVE"));
			adminUser.setIsAdmin(true); // Đặt là admin
			adminUser.setDeleted(false);
			adminUser.setPassword(passwordEncoder.encode("NBT33333"));
			adminUser.setSchool(schoolA);
			userRepository.save(adminUser);
			System.out.println("Đã lưu Admin User: " + adminUser.getFirstName());


			// 3. Lấy và in tất cả User từ DB
			System.out.println(" Tất cả Users trong DB ");
			List<User> allUsers = userRepository.findAll();
			allUsers.forEach(user -> {
				System.out.println("User ID: " + user.getId() + ", Tên: " + user.getFirstName() + " " + user.getLastName() +
						", Email: " + user.getEmail() +
						", Trường: " + (user.getSchool() != null ? user.getSchool().getName() : "Không có"));
			});

			System.out.println("--- Kết thúc khởi tạo dữ liệu demo ---");
		};
	}
}
