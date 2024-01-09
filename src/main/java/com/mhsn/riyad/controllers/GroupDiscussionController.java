package com.mhsn.riyad.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GroupDiscussionController {
    @GetMapping("/show-discussion-list")
    public ModelAndView showDiscussionList() {

        ModelAndView modelAndView = new ModelAndView("discussions/discussion-list");

        return modelAndView;
    }

    @GetMapping("/show-discussion-details")
    public ModelAndView showDiscussionDetails() {

        ModelAndView modelAndView = new ModelAndView("discussions/discussion-details");

        return modelAndView;
    }

    @GetMapping("/show-add-discussion-page")
    public ModelAndView showAddDiscussionPage() {

        ModelAndView modelAndView = new ModelAndView("discussions/add-discussion");

        return modelAndView;
    }

}
