package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.Blog;
import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.repositories.BlogRepository;
import com.mhsn.riyad.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class BlogController {
    @Autowired
    private BlogRepository repository;
    @Autowired
    private UserService userService;

    @GetMapping("/show-blog-list")
    public String showBlogList(Model model, HttpSession httpSession) {

        User user = (User) httpSession.getAttribute("user");
        if (user != null) {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        List<Blog> blogList = repository.findAll();
        model.addAttribute("blogList", blogList);
        return "blogs/blog-list";
    }

    @GetMapping("/show-blog-details")
    public ModelAndView showBlogDetails(@RequestParam Long id) {

        ModelAndView modelAndView = new ModelAndView("blogs/blog-details");

        Optional<Blog> blog = repository.findById(id);

        if (blog.isPresent())
            modelAndView.addObject("blog", blog.get());

        return modelAndView;
    }

    @GetMapping("/show-add-blog-page")
    public ModelAndView showAddBlogPage() {

        ModelAndView modelAndView = new ModelAndView("blogs/add-blog");
        Blog blog = new Blog();
        modelAndView.addObject("blog", blog);
        return modelAndView;
    }

    @PostMapping("/blog-save")
    public String blogSave(@ModelAttribute Blog blog) {

        blog.setPublishedDate(new Date());

        repository.save(blog);

        return "redirect:/show-blog-list";
    }

}
