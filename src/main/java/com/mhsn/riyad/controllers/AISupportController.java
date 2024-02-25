package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.responses.ChatbotResponse;
import com.mhsn.riyad.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AISupportController {
    @Autowired
    private UserService userService;

    @GetMapping("/show-ai-support-page")
    public String showAISupportPage(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login to get AI support");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        return "chatbots/chatbot.html";
    }

    @GetMapping("/api/chatbot")
    public ChatbotResponse getChatbotResponse(@RequestParam String message) {
        ChatbotResponse response = new ChatbotResponse();
        switch (message.toLowerCase()) {
            case "what is mental health?":
                response.setResponse("Mental health includes our emotional, psychological, and social well-being. It affects how we think, feel, and act.");
                break;
            case "how can I improve my mental health?":
                response.setResponse("There are several ways to improve mental health, such as exercising regularly, getting enough sleep, practicing mindfulness, and seeking support from friends or professionals.");
                break;
            case "where can I find help for mental health issues?":
                response.setResponse("You can find help from mental health professionals, such as therapists, counselors, or psychologists. Additionally, there are hotlines and support groups available for immediate assistance.");
            default:
                response.setResponse("I'm sorry, I don't understand. Please ask another question.");
        }
        return response;
    }

}
