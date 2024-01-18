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

    @GetMapping("/")
    public String showIndexPage(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");

        if (user != null) {
            model.addAttribute("user", user);
            userService.setRoleInHttpSession(httpSession, user);
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

            httpSession.setAttribute("user", user.get());

            userService.setRoleInHttpSession(httpSession, user.get());

            return "index";
        }
        else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
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
    public String registration(@ModelAttribute User user) {

        if (Objects.nonNull(user)) {
            userService.saveUser(user);
        }
        return "redirect:/";
    }

}
