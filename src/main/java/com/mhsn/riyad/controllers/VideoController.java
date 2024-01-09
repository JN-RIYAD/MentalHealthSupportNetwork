package com.mhsn.riyad.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class VideoController {
    @GetMapping("/show-video-list")
    public ModelAndView showVideoList() {

        ModelAndView modelAndView = new ModelAndView("videos/video-list");

        return modelAndView;
    }

    @GetMapping("/show-add-video-page")
    public ModelAndView showAddVideoPage() {

        ModelAndView modelAndView = new ModelAndView("videos/add-video");

        return modelAndView;
    }

}
