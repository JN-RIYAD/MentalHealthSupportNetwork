package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

@Controller
public class AuthenticationController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public ModelAndView indexPage() {

        ModelAndView modelAndView = new ModelAndView("index");

        return modelAndView;
    }

    @GetMapping("/show-login-page")
    public ModelAndView showLoginPage() {

        ModelAndView modelAndView = new ModelAndView("login");

        return modelAndView;
    }

    @GetMapping("/show-registration-page")
    public ModelAndView showRegistrationPage() {

        User user = new User();

        ModelAndView modelAndView = new ModelAndView("registration");
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute User user){

        if (Objects.nonNull(user)) {
            if(user.getPassword().equals(user.getConfirmPassword())) {

            }
        }
        return "redirect:/";
    }

}
