package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.User;
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

import java.util.Objects;
import java.util.Optional;

@Controller
public class AuthenticationController {
    @Autowired
    private UserService userService;
    @Autowired
    private HttpSession httpSession;

    private boolean successToastShown = false;

    @GetMapping("/")
    public String showIndexPage(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user != null) {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        return "index";
    }

    @GetMapping("/show-login-page")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        Optional<User> user = userService.findByEmail(email);

        if (user.isPresent() && userService.authenticate(password, user.get().getPassword() )){
            userService.setRoleInModelAndHttpSession(httpSession, model, user.get());
            if (!successToastShown) {
                model.addAttribute("success", "Welcome, " + user.get().getUserName() + "! You have successfully logged in.");
                successToastShown = true; // Set the flag to true
            }
            return "index";
        }
        else {
            model.addAttribute("error", "Invalid email or password...!!!");
            return "login";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:/";
    }

    @GetMapping("/show-registration-page")
    public ModelAndView showRegistrationPage() {
        User user = new User();
        ModelAndView modelAndView = new ModelAndView("registration");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @PostMapping("/registration")
    public String registration(Model model, @ModelAttribute User user) {
        if (Objects.nonNull(user)) {
            Optional<User> existingUser = userService.findByEmail(user.getEmail());
            if (existingUser.isPresent()){
                model.addAttribute("error", "User already exist with this email...!!!");
                return "registration";
            }
            else {
                userService.saveUser(user);
                model.addAttribute("success", "Registered successfully...! Login Now.");
                return "login";
            }
        }
       else {
            model.addAttribute("error", "Invalid Request...!!!");
            return "registration";
        }
    }

}
