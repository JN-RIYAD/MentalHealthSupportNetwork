package com.mhsn.riyad.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthenticationController {
    @GetMapping("/")
    public ModelAndView login() {

        ModelAndView modelAndView = new ModelAndView("/login");

        return modelAndView;
    }
}
