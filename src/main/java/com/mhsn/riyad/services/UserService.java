package com.mhsn.riyad.services;

import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.enums.UserRole;
import com.mhsn.riyad.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveUser(User user) {
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setRole(UserRole.Normal.getLabel());
        user.setRegistrationDate(new Date());
        user.setPassword(encryptedPassword);
        userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public boolean authenticate(String enteredPassword, String storedPasswordHash) {
        return passwordEncoder.matches(enteredPassword, storedPasswordHash);
    }
}