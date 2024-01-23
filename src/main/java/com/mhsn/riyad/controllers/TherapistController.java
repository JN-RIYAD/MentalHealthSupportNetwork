package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.Blog;
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

import java.util.Date;
import java.util.List;


@Controller
public class TherapistController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/show-therapist-list")
    public String showUserList(Model model, HttpSession httpSession) {

        User user = (User) httpSession.getAttribute("user");
        if (user != null) {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        List<User> therapistList = userRepository.findByRole("therapist");
        model.addAttribute("therapistList", therapistList);
        return "therapists/therapist-list";


    }

    @GetMapping("/show-add-therapist-page")
    public String showAddUserPage(Model model, HttpSession httpSession) {

        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to add therapist");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        User therapist = new User();
        model.addAttribute("therapist", therapist);
        return "therapists/add-therapist";
    }


    @PostMapping("/therapist-save")
    public String therapistSave(Model model, HttpSession httpSession, @ModelAttribute User therapist) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to save therapist");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }

        therapist.setRole("therapist");
        therapist.setRegistrationDate(new Date());
        userRepository.save(therapist);

        List<User> therapistList = userRepository.findByRole("therapist");
        model.addAttribute("therapistList", therapistList);
        return "therapists/therapist-list";
    }

    @PostMapping("/therapist-update")
    public String therapistUpdate(Model model, HttpSession httpSession, @ModelAttribute User therapistToUpdate) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to update therapist");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        User savedUser = userRepository.findById(therapistToUpdate.getId()).get();
        savedUser.setRegistrationDate(new Date());
        savedUser.setUserName(therapistToUpdate.getUserName());
        savedUser.setEmail(therapistToUpdate.getEmail());
        savedUser.setAge(therapistToUpdate.getAge());
        savedUser.setMobileNo(therapistToUpdate.getMobileNo());
        savedUser.setAddress(therapistToUpdate.getAddress());

        userRepository.save(savedUser);

        List<User> therapistList = userRepository.findByRole("therapist");;
        model.addAttribute("therapistList", therapistList);
        return "therapists/therapist-list";
    }

    @GetMapping("/therapist-delete")
    public String therapistDelete(Model model, HttpSession httpSession, @RequestParam Long id) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to delete therapist");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        userRepository.deleteById(id);
        List<User> therapistList = userRepository.findByRole("therapist");
        model.addAttribute("therapistList", therapistList);
        return "therapists/therapist-list";
    }


}
