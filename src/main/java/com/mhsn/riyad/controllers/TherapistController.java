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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
public class TherapistController {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/show-therapist-list")
    public String showTherapistList(Model model, HttpSession httpSession) {

        User user = (User) httpSession.getAttribute("user");
        if (user != null) {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        List<User> therapistList = userRepository.findByRole("therapist");
        model.addAttribute("therapistList", therapistList);
        return "therapists/therapist-list";
    }

    @GetMapping("/show-add-therapist-page")
    public String showAddTherapistPage(Model model, HttpSession httpSession) {

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
    public String therapistSave(Model model, HttpSession httpSession, @ModelAttribute User therapist, RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to save therapist");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        //duplicate email check
        Optional<User> existingUserWithCurrentEmail = userRepository.findByEmail(therapist.getEmail());
        if (existingUserWithCurrentEmail.isPresent()) {
            model.addAttribute("error", "Email already exists for another user");
            model.addAttribute("therapist", therapist);
            return "therapists/add-therapist";
        }

        therapist.setRole("therapist");
        therapist.setRegistrationDate(new Date());
        therapist.setPassword(passwordEncoder.encode(therapist.getPassword()));
        userRepository.save(therapist);

        List<User> therapistList = userRepository.findByRole("therapist");
        model.addAttribute("therapistList", therapistList);
        model.addAttribute("success", "Therapist saved successfully");
        redirectAttributes.addFlashAttribute("therapistList", therapistList);
        redirectAttributes.addFlashAttribute("success", "Therapist saved successfully");
        return "redirect:/show-therapist-list";
    }

    @PostMapping("/therapist-update")
    public String therapistUpdate(Model model, HttpSession httpSession, @ModelAttribute User therapistToUpdate, RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to update therapist");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        User savedTherapist = userRepository.findById(therapistToUpdate.getId()).get();
        //duplicate email check
        Optional<User> existingUserWithCurrentEmail = userRepository.findByEmailAndIdNot(therapistToUpdate.getEmail(), therapistToUpdate.getId());
        if (existingUserWithCurrentEmail.isPresent()) {
            model.addAttribute("error", "Email already exists for another user.");
            model.addAttribute("therapistToUpdate", therapistToUpdate);
            return "therapists/update-therapist";
        }
        savedTherapist.setUserName(therapistToUpdate.getUserName());
        savedTherapist.setEmail(therapistToUpdate.getEmail());
        savedTherapist.setFatherName(therapistToUpdate.getFatherName());
        savedTherapist.setMotherName(therapistToUpdate.getMotherName());
        savedTherapist.setDateOfBirth(therapistToUpdate.getDateOfBirth());
        savedTherapist.setAge(therapistToUpdate.getAge());
        savedTherapist.setMobileNo(therapistToUpdate.getMobileNo());
        savedTherapist.setNidNo(therapistToUpdate.getNidNo());
        savedTherapist.setGender(therapistToUpdate.getGender());
        savedTherapist.setReligion(therapistToUpdate.getReligion());
        savedTherapist.setAddress(therapistToUpdate.getAddress());
        savedTherapist.setPasswordForgetQuestionNo(therapistToUpdate.getPasswordForgetQuestionNo());
        savedTherapist.setAnswer(therapistToUpdate.getAnswer());
        userRepository.save(savedTherapist);

        List<User> therapistList = userRepository.findByRole("therapist");
        redirectAttributes.addFlashAttribute("success", "Therapist updated successfully");
        redirectAttributes.addFlashAttribute("therapistList", therapistList);
        return "redirect:/show-therapist-list";
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
    public String therapistDelete(Model model, HttpSession httpSession, @RequestParam Long id, RedirectAttributes redirectAttributes) {
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
        redirectAttributes.addFlashAttribute("therapistList", therapistList);
        return "redirect:/show-therapist-list";
    }


}
