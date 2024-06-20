package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.repositories.UserRepository;
import com.mhsn.riyad.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class AuthenticationController {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private UserService userService;
    @Autowired
    private HttpSession httpSession;
    private boolean successToastShown = false;
    @Autowired
    private UserRepository userRepository;


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

        if (user.isPresent() && userService.authenticate(password, user.get().getPassword())) {
            userService.setRoleInModelAndHttpSession(httpSession, model, user.get());
            if (!successToastShown) {
                model.addAttribute("success", "Welcome, " + user.get().getUserName() + "! You have successfully logged in.");
                System.out.println("Welcome, " + user.get().getUserName() + "! You have successfully logged in.");
                successToastShown = true; // Set the flag to true
            }
            return "index";
        } else {
            model.addAttribute("error", "Invalid email or password...!!!");
            System.out.println("Invalid email or password...!!!");
            return "login";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession httpSession, Model model) {
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
        if (user != null) {
            Optional<User> existingUser = userService.findByEmail(user.getEmail());
            if (existingUser.isPresent()) {
                model.addAttribute("error", "User already exist with this email...!!!");
                return "registration";
            } else {
                userService.saveUser(user);
                model.addAttribute("success", "Registered successfully...! Login Now.");
                return "login";
            }
        } else {
            model.addAttribute("error", "Invalid Request...!!!");
            return "registration";
        }
    }

    @GetMapping("/show-forget-password-page")
    public ModelAndView showForgetPasswordPage(Model model) {
        return new ModelAndView("forget-password");
    }

    @PostMapping("/forgotten-password-change")
    public String changeForgottenPassword(Model model, @RequestParam String email, @RequestParam LocalDate dateOfBirth,
                                          @RequestParam String mobileNo, @RequestParam String nidNo,
                                          @RequestParam Integer passwordForgetQuestionNo, @RequestParam String answer,
                                          @RequestParam String newPassword) {

        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (!user.getDateOfBirth().toString().equals(dateOfBirth.toString())) {
                model.addAttribute("error", "Date of birth not matched");
                return "forget-password";
            } else if (!user.getMobileNo().equals(mobileNo)) {
                model.addAttribute("error", "Mobile number not matched");
                return "forget-password";
            } else if (!user.getNidNo().equals(nidNo)) {
                model.addAttribute("error", "Nid not matched");
                return "forget-password";
            } else if (user.getPasswordForgetQuestionNo() != passwordForgetQuestionNo) {
                model.addAttribute("error", "Password forgetting question not matched");
                return "forget-password";
            } else if (!user.getAnswer().equals(answer)) {
                model.addAttribute("error", "Secret answer not matched");
                return "forget-password";
            } else {
                String encryptedNewPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedNewPassword);
                userRepository.save(user);
                model.addAttribute("success", "Password changed successfully...!!! Login Now.");
                return "login";
            }
        } else {
            model.addAttribute("error", "User not exists with this email");
            return "forget-password";
        }

    }

    @GetMapping("/show-password-change-page")
    public String showPasswordChangePage(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login as user to change password");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        return "password-change";
    }

    @PostMapping("/password-change")
    public String passwordChange(Model model, HttpSession httpSession, @RequestParam
    String oldPassword, @RequestParam String newPassword, RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login to change password");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }

        boolean isMatched = passwordEncoder.matches(oldPassword, user.getPassword());
        if (!isMatched) {
            model.addAttribute("error", "Old password not matched");
            return "password-change";
        }


        String encryptedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encryptedNewPassword);

        userRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "Password changed successfully");
        userService.setRoleInModelAndHttpSession(httpSession, model, user);

        return "redirect:/";
    }

}
