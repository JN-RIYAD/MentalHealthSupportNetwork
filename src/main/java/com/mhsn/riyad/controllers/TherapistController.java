package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.Therapist;
import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.repositories.TherapistRepository;
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
import java.util.Optional;

@Controller
public class TherapistController {

    @Autowired
    private TherapistRepository therapistRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/show-therapist-list")
    public String showTherapistList(Model model, HttpSession httpSession) {

        User user = (User) httpSession.getAttribute("user");
        if (user != null) {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        List<Therapist> therapistList = therapistRepository.findByRole("therapist");
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
        Therapist therapist = new Therapist();
        model.addAttribute("therapist", therapist);
        return "therapists/add-therapist";
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
        Optional<Therapist> therapist = therapistRepository.findById(id);
        model.addAttribute("therapist", therapist.get());
        return "therapists/update-therapist";
    }

    @PostMapping("/therapist-save")
    public String therapistSave(Model model, HttpSession httpSession, @ModelAttribute Therapist therapist) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to save therapist");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        therapist.setAddedDate(new Date());
        therapistRepository.save(therapist);

        List<Therapist> therapistList = therapistRepository.findAll();
        model.addAttribute("therapistList", therapistList);
        return "therapist/therapist-list";
    }

    @PostMapping("/therapist-update")
    public String therapistUpdate(Model model, HttpSession httpSession, @ModelAttribute Therapist therapist) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to update therapist");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        therapist.setUpdatedDate(new Date());
        therapistRepository.save(therapist);

        List<Therapist> therapistList = therapistRepository.findAll();
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
        therapistRepository.deleteById(id);
        List<Therapist> therapistList = therapistRepository.findAll();
        model.addAttribute("therapistList", therapistList);
        return "therapists/therapist-list";
    }




}
