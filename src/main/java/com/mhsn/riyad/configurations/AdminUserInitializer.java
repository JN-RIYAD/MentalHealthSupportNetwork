package com.mhsn.riyad.configurations;

import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

@Configuration
public class AdminUserInitializer {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        String adminEmail = "admin@gmail.com";

        // Check if admin user already exists
        Optional<User> existingAdmin = userRepository.findByEmail(adminEmail);

        if (existingAdmin.isEmpty()) {
            // Admin user doesn't exist, create a new one
            User admin = new User();
            admin.setAddress("Dhaka");
            admin.setAge(25);
            admin.setEmail(adminEmail);
            admin.setGender("Male");
            admin.setMobileNo("01744330734");
            admin.setPassword(passwordEncoder.encode("123456")); // Use a password encoder
            admin.setRegistrationDate(new Date());
            admin.setRole("admin");
            admin.setUserName("Riyad");
            userRepository.save(admin);
        } else {
            User admin = existingAdmin.get();
            admin.setAddress("Dhaka");
            admin.setAge(25);
            admin.setEmail(adminEmail);
            admin.setGender("Male");
            admin.setMobileNo("01744330734");
            admin.setPassword(passwordEncoder.encode("123456")); // Use a password encoder
            admin.setRegistrationDate(new Date());
            admin.setRole("admin");
            admin.setUserName("Riyad");
            userRepository.save(admin);
        }
    }
}
