package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.repositories.UserRepository;
import com.mhsn.riyad.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


@Controller
public class ProfileController {
    @Autowired
    private UserService userService;
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
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM, yyyy");
        String formattedDateOfBirth = formatter.format(user.getDateOfBirth());
        model.addAttribute("formattedDateOfBirth", formattedDateOfBirth);

        Map<Integer, String> questions = new HashMap<>();
        questions.put(1, "What is Your Favourite Flower?");
        questions.put(2, "What is Your Favourite Place?");
        questions.put(3, "What is Your Favourite Movie Name?");
        questions.put(4, "What is your hobby?");
        questions.put(5, "Your own secret question.");
        questions.put(null, "");

        // Get the selected question based on passwordForgetQuestionNo
        String selectedQuestion = questions.get(user.getPasswordForgetQuestionNo());

        model.addAttribute("selectedQuestion", selectedQuestion);
        return "profiles/profile";
    }

    @GetMapping("/show-update-profile-page")
    public String showUpdateProfilePage(Model model, HttpSession httpSession, @RequestParam Long id) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
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
        savedUser.setFatherName(userToUpdate.getFatherName());
        savedUser.setMotherName(userToUpdate.getMotherName());
        savedUser.setDateOfBirth(userToUpdate.getDateOfBirth());
        savedUser.setAge(userToUpdate.getAge());
        savedUser.setMobileNo(userToUpdate.getMobileNo());
        savedUser.setNidNo(userToUpdate.getNidNo());
        savedUser.setGender(userToUpdate.getGender());
        savedUser.setReligion(userToUpdate.getReligion());
        savedUser.setAddress(userToUpdate.getAddress());
        savedUser.setPasswordForgetQuestionNo(userToUpdate.getPasswordForgetQuestionNo());
        savedUser.setAnswer(userToUpdate.getAnswer());

        userRepository.save(savedUser);

        userService.setRoleInModelAndHttpSession(httpSession, model, savedUser);

        return "redirect:/show-profile-page";
    }
}

