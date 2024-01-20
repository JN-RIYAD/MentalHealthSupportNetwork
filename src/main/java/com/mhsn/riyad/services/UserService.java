package com.mhsn.riyad.services;

import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
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


    public void setRoleInModelAndHttpSession(HttpSession httpSession, Model model, User user) {

        model.addAttribute("username", user.getUserName());
        model.addAttribute("userId", user.getId());
        model.addAttribute("user", user);

        httpSession.setAttribute("user", user);

        if (user.getRole().equals("admin")) {
            model.addAttribute("isAdmin", true);
            httpSession.setAttribute("isAdmin", true);
        } else if (user.getRole().equals("therapist")) {
            model.addAttribute("isTherapist", true);
            httpSession.setAttribute("isTherapist", true);
        } else {
            model.addAttribute("isUser", true);
            httpSession.setAttribute("isUser", true);
        }
    }


    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }
}