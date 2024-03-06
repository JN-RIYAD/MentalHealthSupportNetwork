package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.ChatBotQuestionAnswer;
import com.mhsn.riyad.entities.NotAnsweredQuestion;
import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.repositories.ChatBotQuestionAnswerRepository;
import com.mhsn.riyad.repositories.NotAnsweredQuestionRepository;
import com.mhsn.riyad.repositories.UserChatBotHistoryRepository;
import com.mhsn.riyad.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
public class ChatBotQuestionAnswerController {
    @Autowired
    private UserService userService;

    @Autowired
    private ChatBotQuestionAnswerRepository chatBotQuestionAnswerRepository;

    @GetMapping("/show-chatbot-question-answer-list")
    public String shoChatBotQuestionList(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as admin to access Chat-Bot configuration");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        List<ChatBotQuestionAnswer> chatBotQuestionAnswerList = chatBotQuestionAnswerRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("chatBotQuestionAnswerList", chatBotQuestionAnswerList);
        return "chatbots/chatbot-question-answer-list";
    }

    @GetMapping("/show-chatbot-question-answer-add-page")
    public String showChatBotQuestionAnswerAddPage(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to add chatbot question answer");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }

        ChatBotQuestionAnswer chatBotQuestionAnswer = new ChatBotQuestionAnswer();

        model.addAttribute("chatBotQuestionAnswer", chatBotQuestionAnswer);

        return "chatbots/chatbot-question-answer-add";
    }

    @PostMapping("/chatbot-question-answer-save")
    public String chatBotQuestionAnswerSave(Model model, HttpSession httpSession, @ModelAttribute ChatBotQuestionAnswer chatBotQuestionAnswer, RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to save chatbot question answer");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        chatBotQuestionAnswer.setCreatedAt(new Date());
        chatBotQuestionAnswerRepository.save(chatBotQuestionAnswer);

        List<ChatBotQuestionAnswer> chatBotQuestionAnswerList = chatBotQuestionAnswerRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        redirectAttributes.addFlashAttribute("chatBotQuestionAnswerList", chatBotQuestionAnswerList);
        return "redirect:/show-chatbot-question-answer-list";
    }

    @GetMapping("/show-chatbot-question-answer-update-page")
    public String showChatBotQuestionAnswerUpdatePage(Model model, HttpSession httpSession, @RequestParam Long id) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to update chatbot question answer");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }

        ChatBotQuestionAnswer chatBotQuestionAnswerToUpdate = chatBotQuestionAnswerRepository.findById(id).get();

        model.addAttribute("chatBotQuestionAnswerToUpdate", chatBotQuestionAnswerToUpdate);

        return "chatbots/chatbot-question-answer-update";
    }
    @PostMapping("/chatbot-question-answer-update")
    public String chatBotQuestionAnswerUpdate(Model model, HttpSession httpSession, @ModelAttribute ChatBotQuestionAnswer chatBotQuestionAnswerToUpdate, RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to update answer of chatbot question");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        chatBotQuestionAnswerToUpdate.setCreatedAt(new Date());
        chatBotQuestionAnswerRepository.save(chatBotQuestionAnswerToUpdate);

        List<ChatBotQuestionAnswer> chatBotQuestionAnswerList = chatBotQuestionAnswerRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        redirectAttributes.addFlashAttribute("chatBotQuestionAnswerList", chatBotQuestionAnswerList);
        return "redirect:/show-chatbot-question-answer-list";
    }

    @GetMapping("/chatbot-question-answer-delete")
    public String chatbotQuestionAnswerDelete(Model model, HttpSession httpSession, @RequestParam Long id, RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to delete chatbot question answer");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        chatBotQuestionAnswerRepository.deleteById(id);
        List<ChatBotQuestionAnswer> chatBotQuestionAnswerList = chatBotQuestionAnswerRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("chatBotQuestionAnswerList", chatBotQuestionAnswerList);
        redirectAttributes.addFlashAttribute("chatBotQuestionAnswerList", chatBotQuestionAnswerList);
        return "redirect:/show-chatbot-question-answer-list";
    }

}
