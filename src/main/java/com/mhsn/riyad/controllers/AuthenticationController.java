package com.mhsn.riyad.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthenticationController {
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

        ModelAndView modelAndView = new ModelAndView("registration");

        return modelAndView;
    }

}
