package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.Blog;
import com.mhsn.riyad.entities.Podcast;
import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.repositories.PodcastRepository;
import com.mhsn.riyad.repositories.UserRepository;
import com.mhsn.riyad.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Controller
public class ProfileController {
    @Autowired
    private UserService userService;
    @Value("${upload.base-dir}")
    private String baseUploadDir;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/show-profile-page")
    public String showProfilePage(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login to view profile");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        return "profiles/profile";
    }

    @GetMapping("/show-update-profile-page")
    public String showEditProfilePage(Model model, HttpSession httpSession, @RequestParam Long id) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null ) {
            model.addAttribute("error", "Login to update Profile");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        User userToUpdate = userRepository.findById(id).get();
        model.addAttribute("userToUpdate", userToUpdate);
        return "profiles/update-profile";
    }


        @PostMapping("/update-profile")
        public String profileUpdate(Model model, HttpSession httpSession, @ModelAttribute User userToUpdate, RedirectAttributes redirectAttributes) {
            User user = (User) httpSession.getAttribute("user");
            if (user == null) {
                model.addAttribute("error", "Login to update profile");
                return "login";
            } else {
                userService.setRoleInModelAndHttpSession(httpSession, model, user);
            }
            User savedUser = userRepository.findById(user.getId()).get();
            savedUser.setUserName(userToUpdate.getUserName());
            savedUser.setEmail(userToUpdate.getEmail());
            savedUser.setAge(userToUpdate.getAge());
            savedUser.setMobileNo(userToUpdate.getMobileNo());
            savedUser.setGender(userToUpdate.getGender());
            savedUser.setAddress(userToUpdate.getAddress());
            userRepository.save(savedUser);

            userService.setRoleInModelAndHttpSession(httpSession, model, savedUser);

            return "profiles/profile";
        }
}

