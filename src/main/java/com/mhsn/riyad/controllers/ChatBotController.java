package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.*;
import com.mhsn.riyad.repositories.ChatBotQuestionAnswerRepository;
import com.mhsn.riyad.repositories.DiscussionRepository;
import com.mhsn.riyad.repositories.NotAnsweredQuestionRepository;
import com.mhsn.riyad.repositories.UserChatBotHistoryRepository;
import com.mhsn.riyad.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
public class ChatBotController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserChatBotHistoryRepository userChatBotHistoryRepository;

    @Autowired
    private ChatBotQuestionAnswerRepository chatBotQuestionAnswerRepository;
    @Autowired
    private NotAnsweredQuestionRepository notAnsweredQuestionRepository;

    @GetMapping("/show-chat-list")
    public String showChatList(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to access Chat-Bot support");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        List<UserChatBotHistory> chatList = userChatBotHistoryRepository.findByUserId(user.getId());
        model.addAttribute("chatList", chatList);
        UserChatBotHistory userChatBotHistory = new UserChatBotHistory();
        model.addAttribute("userChatBotHistory", userChatBotHistory);
        return "chatbots/chat-list";
    }

    @Transactional
    @PostMapping("/chat-bot")
    public String newChatSave(Model model, HttpSession httpSession, @ModelAttribute UserChatBotHistory userChatBotHistory, @RequestParam Long userId, RedirectAttributes redirectAttributes){
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to access Chat-Bot support");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        // Get the most matched question from ChatBotQuestionAnswer table
        ChatBotQuestionAnswer chatBotQuestionAnswer = getMostMatchedQuestionAnswer(userChatBotHistory.getQuestion(), user);

        // Save the UserChatBotHistory object
        userChatBotHistory.setUserId(userId);
        userChatBotHistory.setChatBotQuestionAnswer(chatBotQuestionAnswer);
        userChatBotHistoryRepository.save(userChatBotHistory);

        List<UserChatBotHistory> chatList = userChatBotHistoryRepository.findByUserId(userId);
        redirectAttributes.addFlashAttribute("chatList", chatList);
        return "redirect:/show-chat-list";
    }

    private ChatBotQuestionAnswer getMostMatchedQuestionAnswer(String message, User user) {
        List<ChatBotQuestionAnswer> questionAnswerList = chatBotQuestionAnswerRepository.findAll();
        ChatBotQuestionAnswer mostMatchedQuestionAnswer = null;
        double maxSimilarity = 0.0;

        for (ChatBotQuestionAnswer questionAnswer : questionAnswerList) {
            double similarity = calculateJaccardSimilarity(message, questionAnswer.getQuestion());
            if (similarity >= 0.4 && similarity > maxSimilarity) {
                maxSimilarity = similarity;
                mostMatchedQuestionAnswer = questionAnswer;
            }
        }

        // If no question matched at least 40%, return the default not matched question
        if (mostMatchedQuestionAnswer == null) {
            mostMatchedQuestionAnswer = chatBotQuestionAnswerRepository.findById(1L).orElse(null);

            //save this message to NotAnsweredQuestion table for admin to answer
            NotAnsweredQuestion notAnsweredQuestion = new NotAnsweredQuestion();
            notAnsweredQuestion.setQuestion(message);
            notAnsweredQuestion.setUser(user);
            notAnsweredQuestionRepository.save(notAnsweredQuestion);
        }

        return mostMatchedQuestionAnswer;
    }

    private double calculateJaccardSimilarity(String message, String question) {
        Set<String> messageWords = new HashSet<>(Arrays.asList(message.split(" ")));
        Set<String> questionWords = new HashSet<>(Arrays.asList(question.split(" ")));

        Set<String> intersection = new HashSet<>(messageWords);
        intersection.retainAll(questionWords);

        Set<String> union = new HashSet<>(messageWords);
        union.addAll(questionWords);

        return (double) intersection.size() / union.size();
    }

}
