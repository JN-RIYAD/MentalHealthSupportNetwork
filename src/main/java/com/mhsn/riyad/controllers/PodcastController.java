package com.mhsn.riyad.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PodcastController {
    @GetMapping("/show-podcast-list")
    public ModelAndView showPodcastList() {

        ModelAndView modelAndView = new ModelAndView("podcasts/podcast-list");

        return modelAndView;
    }

    @GetMapping("/show-add-podcast-page")
    public ModelAndView showAddPodcastPage() {

        ModelAndView modelAndView = new ModelAndView("podcasts/add-podcast");

        return modelAndView;
    }

}
