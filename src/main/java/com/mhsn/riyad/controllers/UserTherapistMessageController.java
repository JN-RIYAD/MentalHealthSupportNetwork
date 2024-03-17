package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.entities.UserTherapistMessage;
import com.mhsn.riyad.repositories.UserTherapistMessageRepository;
import com.mhsn.riyad.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
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
        List<UserTherapistMessage> userTherapistMessageList = userTherapistMessageRepository.findBySenderIdAndReceiverId(user.getId(), receiverId);
        model.addAttribute("userTherapistMessageList", userTherapistMessageList);
        UserTherapistMessage userTherapistMessage = new UserTherapistMessage();
        model.addAttribute("userTherapistMessage", userTherapistMessage);
        return "messages/message-list";
    }

    @GetMapping("/clear-messages")
    public String clearMessages(Model model, HttpSession httpSession, @RequestParam Long receiverId, RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login to clear previous chat list");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        userTherapistMessageRepository.clearMessagesBySenderIdAndReceiverId(user.getId(), receiverId);

        List<UserTherapistMessage> userTherapistMessageList = userTherapistMessageRepository.findBySenderIdAndReceiverId(user.getId(), receiverId);
        redirectAttributes.addFlashAttribute("userTherapistMessageList", userTherapistMessageList);
        UserTherapistMessage userTherapistMessage = new UserTherapistMessage();
        redirectAttributes.addFlashAttribute("userTherapistMessage", userTherapistMessage);
        return "redirect:/show-user-therapist-message-list?receiverId=" + receiverId;
    }


    @Transactional
    @PostMapping("/new-message-save")
    public String newMessageSave(Model model, HttpSession httpSession, @ModelAttribute UserTherapistMessage userTherapistMessageToSave, @RequestParam Long receiverId, RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to chat with therapist");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        userTherapistMessageToSave.setSenderId(user.getId());
        userTherapistMessageToSave.setReceiverId(receiverId);
        userTherapistMessageToSave.setSentAt(new Date());

        userTherapistMessageRepository.save(userTherapistMessageToSave);

        List<UserTherapistMessage> userTherapistMessageList = userTherapistMessageRepository.findBySenderIdAndReceiverId(user.getId(), receiverId);
        redirectAttributes.addFlashAttribute("userTherapistMessageList", userTherapistMessageList);
        UserTherapistMessage userTherapistMessage = new UserTherapistMessage();
        redirectAttributes.addFlashAttribute("userTherapistMessage", userTherapistMessage);
        return "redirect:/show-user-therapist-message-list?receiverId=" + receiverId;
    }

}
