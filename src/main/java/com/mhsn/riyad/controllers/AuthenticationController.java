package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.enums.UserRole;
import com.mhsn.riyad.repositories.UserRepository;
import com.mhsn.riyad.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.Objects;

@Controller
public class AuthenticationController {
    @Autowired
    private UserService userService;
    @Autowired
    private HttpSession httpSession;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public ModelAndView indexPage(Model model, HttpSession httpSession) {

        User user = (User) httpSession.getAttribute("user");

        ModelAndView modelAndView = new ModelAndView("index");

        if (user != null) {
            model.addAttribute("user", user);
            if (httpSession.getAttribute("isAdmin") != null) {
                model.addAttribute("isAdmin", true);
            }
        }
        modelAndView.addObject(model);

        return modelAndView;
    }

    @GetMapping("/show-login-page")
    public ModelAndView showLoginPage(Model model) {

        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject(model);

        return modelAndView;
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {

        User user = userService.getUserByEmail(email);

        if (user != null && userService.authenticate(password, user.getPassword())) {
            httpSession.setAttribute("user", user);

            if (user.getRole().equals(UserRole.Admin.getLabel())) {
                httpSession.setAttribute("isAdmin", true);
            }
            else if (user.getRole().equals(UserRole.Therapist.getLabel())) {
                httpSession.setAttribute("isTherapist", true);
            }
            else if (user.getRole().equals(UserRole.Normal.getLabel())){
                httpSession.setAttribute("isNormal", true);
            }
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "redirect:/show-login-page";
        }
    }

    @GetMapping("/logout")
    public String logout() {
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
    public String registration(@ModelAttribute User user){

        if (Objects.nonNull(user)) {

        }
        return "redirect:/";
    }

}
