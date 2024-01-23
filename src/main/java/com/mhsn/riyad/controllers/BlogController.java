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

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class BlogController {
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/show-blog-list")
    public String showBlogList(Model model, HttpSession httpSession) {

        User user = (User) httpSession.getAttribute("user");
        if (user != null) {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        List<Blog> blogList = blogRepository.findAll(); //findByRole("therapist");
        model.addAttribute("blogList", blogList);
        return "blogs/blog-list";
    }

    @GetMapping("/show-add-blog-page")
    public String showAddBlogPage(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to save blog");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        Blog blog = new Blog();
        model.addAttribute("blog", blog);
        return "blogs/add-blog";
    }

    @GetMapping("/show-blog-details")
    public String showBlogDetails(Model model, HttpSession httpSession, @RequestParam Long id) {

        User user = (User) httpSession.getAttribute("user");
        if (user != null) {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }

        Optional<Blog> blog = blogRepository.findById(id);

        model.addAttribute("blog", blog.get());

        return "blogs/blog-details";
    }

    @GetMapping("/show-update-blog-page")
    public String showEditBlogPage(Model model, HttpSession httpSession, @RequestParam Long id) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to update blog");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        Blog blogToUpdate = blogRepository.findById(id).get();
        model.addAttribute("blogToUpdate", blogToUpdate);
        return "blogs/update-blog";
    }

    @PostMapping("/blog-save")
    public String blogSave(Model model, HttpSession httpSession, @ModelAttribute Blog blog) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to save blog");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        blog.setPublishedDate(new Date());
        blogRepository.save(blog);

        List<Blog> blogList = blogRepository.findAll();
        model.addAttribute("blogList", blogList);
        return "blogs/blog-list";
    }

    @PostMapping("/blog-update")
    public String blogUpdate(Model model, HttpSession httpSession, @ModelAttribute Blog blogToUpdate) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to update blog");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        Blog savedBlog = blogRepository.findById(blogToUpdate.getId()).get();
        savedBlog.setUpdatedDate(new Date());
        savedBlog.setTitle(blogToUpdate.getTitle());
        savedBlog.setTopic(blogToUpdate.getTopic());
        savedBlog.setAuthor(blogToUpdate.getAuthor());
        savedBlog.setDescription(blogToUpdate.getDescription());
        
        blogRepository.save(savedBlog);

        List<Blog> blogList = blogRepository.findAll();
        model.addAttribute("blogList", blogList);
        return "blogs/blog-list";
    }

    @GetMapping("/blog-delete")
    public String blogDelete(Model model, HttpSession httpSession, @RequestParam Long id) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to delete blog");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        blogRepository.deleteById(id);
        List<Blog> blogList = blogRepository.findAll();
        model.addAttribute("blogList", blogList);
        return "blogs/blog-list";
    }

}
