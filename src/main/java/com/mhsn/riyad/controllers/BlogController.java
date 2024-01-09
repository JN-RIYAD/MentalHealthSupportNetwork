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

    @GetMapping("/show-blog-details")
    public ModelAndView showBlogDetails() {

        ModelAndView modelAndView = new ModelAndView("blogs/blog-details");

        return modelAndView;
    }

    @GetMapping("/show-add-blog-page")
    public ModelAndView showAddBlogPage() {

        ModelAndView modelAndView = new ModelAndView("blogs/add-blog");

        return modelAndView;
    }

}
