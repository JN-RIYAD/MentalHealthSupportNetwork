package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.entities.UserTherapistMessage;
import com.mhsn.riyad.repositories.UserTherapistMessageRepository;
import com.mhsn.riyad.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserTherapistMessageController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserTherapistMessageRepository userTherapistMessageRepository;

    @GetMapping("/show-user-therapist-message-list")
    public String showChatList(Model model, HttpSession httpSession, @RequestParam Long receiverId) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to chat with therapist");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        User receiver = userService.findById(receiverId).get();

        List<UserTherapistMessage> userTherapistMessageList = userTherapistMessageRepository.findBySenderIdAndReceiverId(user.getId(), receiverId);

        model.addAttribute("userTherapistMessageList", userTherapistMessageList);
        UserTherapistMessage userTherapistMessage = new UserTherapistMessage();
        userTherapistMessage.setReceiver(receiver);
        model.addAttribute("userTherapistMessage", userTherapistMessage);
        return "messages/message-list";
    }

}
