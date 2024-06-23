package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.entities.UserTherapistMessage;
import com.mhsn.riyad.repositories.UserRepository;
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

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class UserTherapistMessageController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserTherapistMessageRepository userTherapistMessageRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/show-user-therapist-message-list")
    public String showUserTherapistMessageList(Model model, HttpSession httpSession, @RequestParam Long receiverId) {
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
        if(user.getRole().equals("user")){
            User therapist = userService.findById(receiverId).get();
            model.addAttribute("therapistName", therapist.getUserName());
        }
        else{
            User patient = userService.findById(receiverId).get();
            model.addAttribute("patientName", patient.getUserName());
        }

        return "messages/message-list";
    }

    @Transactional
    @PostMapping("/new-message-save")
    public String newMessageSave(Model model, HttpSession httpSession, @ModelAttribute UserTherapistMessage userTherapistMessageToSave, @RequestParam Long receiverId, RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to chat with user/therapist");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        if(user.getRole().equals("user")){
            if(user.getBalance() < 100) {
                redirectAttributes.addFlashAttribute("error", "Insufficient Balance... Pay First");
                return "redirect:/show-user-therapist-message-list?receiverId=" + receiverId;
            }
            else{
                user.setBalance(user.getBalance() - 100);
                User receiver = userService.findById(receiverId).get();
                userTherapistMessageToSave.setSender(user);
                userTherapistMessageToSave.setReceiver(receiver);
                userTherapistMessageToSave.setSentAt(LocalDateTime.now());
                userTherapistMessageRepository.save(userTherapistMessageToSave);
                userRepository.save(user);
                redirectAttributes.addFlashAttribute("success", "Message sent successfully. BDT-100tk has been deducted");
                return "redirect:/show-user-therapist-message-list?receiverId=" + receiverId;
            }
        }
        else {
            User receiver = userService.findById(receiverId).get();
            userTherapistMessageToSave.setSender(user);
            userTherapistMessageToSave.setReceiver(receiver);
            userTherapistMessageToSave.setSentAt(LocalDateTime.now());

            userTherapistMessageRepository.save(userTherapistMessageToSave);

            redirectAttributes.addFlashAttribute("success", "Message sent successfully.");
            return "redirect:/show-user-therapist-message-list?receiverId=" + receiverId;
        }
    }

    @GetMapping("/show-last-conversation-list")
    public String getUserMessages(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to see message list");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        List<UserTherapistMessage> userTherapistLastMessageList = userTherapistMessageRepository.getLastMessageListByUserId(user.getId());

        model.addAttribute("userTherapistLastMessageList", userTherapistLastMessageList);
        return "messages/last-conversation-list";
    }

}
