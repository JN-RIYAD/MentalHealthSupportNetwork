package com.mhsn.riyad.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthenticationController {
    @GetMapping("/")
    public ModelAndView indexPage() {

        ModelAndView modelAndView = new ModelAndView("/index");

        return modelAndView;
    }

    @GetMapping("/show-login-page")
    public ModelAndView showLoginPage() {

        ModelAndView modelAndView = new ModelAndView("/login");

        return modelAndView;
    }

    @GetMapping("/show-user-registration-page")
    public ModelAndView showUserRegistrationPage() {

        ModelAndView modelAndView = new ModelAndView("/user-registration");

        return modelAndView;
    }

}
