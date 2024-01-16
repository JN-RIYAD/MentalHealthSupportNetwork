package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.Blog;
import com.mhsn.riyad.repositories.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class BlogController {
    @Autowired
    private BlogRepository repository;

    @GetMapping("/show-blog-list")
    public ModelAndView showBlogList() {

        ModelAndView modelAndView = new ModelAndView("blogs/blog-list");
        List<Blog> blogList = repository.findAll();
        modelAndView.addObject(blogList);

        return modelAndView;
    }

    @GetMapping("/show-blog-details/{id}")
    public ModelAndView showBlogDetails(@PathVariable Long id) {

        ModelAndView modelAndView = new ModelAndView("blogs/blog-details");

        Optional<Blog> blog = repository.findById(id);

        if (blog.isPresent())
            modelAndView.addObject(blog.get());

        return modelAndView;
    }

    @GetMapping("/show-add-blog-page")
    public ModelAndView showAddBlogPage() {

        ModelAndView modelAndView = new ModelAndView("blogs/add-blog");
        Blog blog = new Blog();
        modelAndView.addObject(blog);

        return modelAndView;
    }

    @PostMapping("/blog-save")
    public String blogSave(@RequestBody Blog blog) {

        blog.setPublishedDate(new Date());

        repository.save(blog);

        return "redirect:/show-blog-list";
    }

}
