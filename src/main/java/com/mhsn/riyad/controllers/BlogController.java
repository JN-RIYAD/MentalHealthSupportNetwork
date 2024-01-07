package com.mhsn.riyad.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BlogController {
    @GetMapping("/show-blog-list")
    public ModelAndView showBlogList() {

        ModelAndView modelAndView = new ModelAndView("blogs/blog-list");

        return modelAndView;
    }

}
