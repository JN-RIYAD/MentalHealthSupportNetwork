package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.*;
import com.mhsn.riyad.repositories.ChatBotQuestionAnswerRepository;
import com.mhsn.riyad.repositories.NotAnsweredQuestionRepository;
import com.mhsn.riyad.repositories.UserChatBotHistoryRepository;
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

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ChatBotConfigurationController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserChatBotHistoryRepository userChatBotHistoryRepository;

    @Autowired
    private ChatBotQuestionAnswerRepository chatBotQuestionAnswerRepository;
    @Autowired
    private NotAnsweredQuestionRepository notAnsweredQuestionRepository;

    @GetMapping("/show-not-answered-list")
    public String showNotAnsweredChatList(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as admin to access Chat-Bot configuration");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        List<NotAnsweredQuestion> notAnsweredQuestionList = notAnsweredQuestionRepository.findAll();
        model.addAttribute("notAnsweredQuestionList", notAnsweredQuestionList);
        return "chatbots/not-answered-list";
    }

    @GetMapping("/show-set-answer-page")
    public String showSetAnswerPage(Model model, HttpSession httpSession, @RequestParam Long notAnsweredQuestionId) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to set not answered question");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        NotAnsweredQuestion notAnsweredQuestion = notAnsweredQuestionRepository.findById(notAnsweredQuestionId).get();

        ChatBotQuestionAnswer chatBotQuestionAnswer = new ChatBotQuestionAnswer();
        chatBotQuestionAnswer.setQuestion(notAnsweredQuestion.getQuestion());

        model.addAttribute("chatBotQuestionAnswer", chatBotQuestionAnswer);
        model.addAttribute("notAnsweredQuestionId", notAnsweredQuestionId);

        return "chatbots/set-chatbot-question-answer";
    }


    @PostMapping("/chatbot-question-answer-save")
    public String chatBotQuestionAnswerSave(Model model, HttpSession httpSession, @ModelAttribute ChatBotQuestionAnswer chatBotQuestionAnswer,  @RequestParam Long notAnsweredQuestionId, RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to answer chatbot question");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        chatBotQuestionAnswer.setCreatedAt(new Date());
        chatBotQuestionAnswerRepository.save(chatBotQuestionAnswer);

        notAnsweredQuestionRepository.deleteById(notAnsweredQuestionId);

        List<NotAnsweredQuestion> notAnsweredQuestionList = notAnsweredQuestionRepository.findAll();
        redirectAttributes.addFlashAttribute("notAnsweredQuestionList", notAnsweredQuestionList);
        return "redirect:/show-not-answered-list";
    }

    @GetMapping("/not-answered-question-delete")
    public String notAnsweredQuestionDelete(Model model, HttpSession httpSession, @RequestParam Long id, RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to delete not answered question");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        notAnsweredQuestionRepository.deleteById(id);
        List<NotAnsweredQuestion> notAnsweredQuestionList = notAnsweredQuestionRepository.findAll();
        model.addAttribute("notAnsweredQuestionList", notAnsweredQuestionList);
        redirectAttributes.addFlashAttribute("notAnsweredQuestionList", notAnsweredQuestionList);
        return "redirect:/show-not-answered-list";
    }


}
