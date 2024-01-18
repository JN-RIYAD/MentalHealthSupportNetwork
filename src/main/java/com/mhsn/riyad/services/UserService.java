package com.mhsn.riyad.services;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private final Map<String, User> users = new HashMap<>();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public boolean authenticate(String password, String encodedPassword) {
            return passwordEncoder.matches(password, encodedPassword);
    }
    public void saveUser(User user) {
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setRole("user");
        user.setRegistrationDate(new Date());
        user.setPassword(encryptedPassword);
        userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void setRoleInHttpSession(HttpSession httpSession, User user) {
        if (user.getRole().equals("admin")) {
            httpSession.setAttribute("isAdmin", true);
        }
        else if (user.getRole().equals("therapist")) {
            httpSession.setAttribute("isTherapist", true);
        }
        else{
            httpSession.setAttribute("isUser", true);
        }
    }

}