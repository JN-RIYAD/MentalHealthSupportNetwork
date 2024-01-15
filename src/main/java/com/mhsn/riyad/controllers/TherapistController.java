package com.mhsn.riyad.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TherapistController {
    @GetMapping("/show-therapist-list")
    public ModelAndView showTherapistList() {

        ModelAndView modelAndView = new ModelAndView("therapists/therapist-list");

        return modelAndView;
    }

    @GetMapping("/show-therapist-details")
    public ModelAndView showTherapistDetails() {

        ModelAndView modelAndView = new ModelAndView("therapists/therapist-details");

        return modelAndView;
    }

    @GetMapping("/show-add-therapist-page")
    public ModelAndView showAddTherapistPage() {

        ModelAndView modelAndView = new ModelAndView("therapists/add-therapist");

        return modelAndView;
    }
}
