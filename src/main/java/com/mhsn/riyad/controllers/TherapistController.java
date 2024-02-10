package com.mhsn.riyad.controllers;
import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.repositories.UserRepository;
import com.mhsn.riyad.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
        //TODO: duplicate email check
        therapist.setRole("therapist");
        therapist.setRegistrationDate(new Date());
        therapist.setPassword(passwordEncoder.encode(therapist.getPassword()));
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
        User savedTherapist = userRepository.findById(therapistToUpdate.getId()).get();
        //TODO: duplicate email check
        savedTherapist.setUserName(therapistToUpdate.getUserName());
        savedTherapist.setEmail(therapistToUpdate.getEmail());
        savedTherapist.setAge(therapistToUpdate.getAge());
        savedTherapist.setMobileNo(therapistToUpdate.getMobileNo());
        savedTherapist.setAddress(therapistToUpdate.getAddress());
        savedTherapist.setPassword(passwordEncoder.encode(therapistToUpdate.getPassword()));
        userRepository.save(savedTherapist);

        List<User> therapistList = userRepository.findByRole("therapist");;
        model.addAttribute("therapistList", therapistList);
        return "therapists/therapist-list";
    }

    @GetMapping("/show-update-therapist-page")
    public String showEditTherapistPage(Model model, HttpSession httpSession, @RequestParam Long id) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to update therapist");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        User therapistToUpdate = userRepository.findById(id).get();
        model.addAttribute("therapistToUpdate", therapistToUpdate);
        return "therapists/update-therapist";
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
