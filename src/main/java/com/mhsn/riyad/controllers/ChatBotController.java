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
        List<UserChatBotHistory> chatList = userChatBotHistoryRepository.findByUserIdOrderByIdDesc(user.getId());
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
        userChatBotHistory.setCreatedAt(new Date());
        userChatBotHistoryRepository.save(userChatBotHistory);

        List<UserChatBotHistory> chatList = userChatBotHistoryRepository.findByUserIdOrderByIdDesc(userId);
        redirectAttributes.addFlashAttribute("chatList", chatList);
        return "redirect:/show-chat-list";
    }

    private ChatBotQuestionAnswer getMostMatchedQuestionAnswer(String message, User user) {
        // Define words to remove
        Set<String> wordsToRemove = new HashSet<>(Arrays.asList(
                "what", "who", "why", "am", "is", "are", "where", "how", "a", "an", "the", "?", "!", "@", "%", "&", ",", ":", ";"
        ));
        List<ChatBotQuestionAnswer> questionAnswerList = chatBotQuestionAnswerRepository.findAll();
        ChatBotQuestionAnswer mostMatchedQuestionAnswer = null;
        double maxSimilarity = 0.0;

        for (ChatBotQuestionAnswer questionAnswer : questionAnswerList) {
            double similarity = calculateJaccardSimilarity(message, questionAnswer.getQuestion(), wordsToRemove);
            if (similarity > maxSimilarity) {
                int matchingWords = countMatchingWords(message, questionAnswer.getQuestion(), wordsToRemove);
                int messageWordCount = countWords(message, wordsToRemove);
                if ((double) matchingWords / messageWordCount >= 0.4) {
                    maxSimilarity = similarity;
                    mostMatchedQuestionAnswer = questionAnswer;
                }
            }
        }

        // If no question matched at least 40%, return the default not matched question
        if (mostMatchedQuestionAnswer == null) {
            mostMatchedQuestionAnswer = chatBotQuestionAnswerRepository.findById(1L).orElse(null);

            // Save this message to NotAnsweredQuestion table for admin to answer
            NotAnsweredQuestion notAnsweredQuestion = new NotAnsweredQuestion();
            notAnsweredQuestion.setQuestion(message);
            notAnsweredQuestion.setUser(user);
            notAnsweredQuestionRepository.save(notAnsweredQuestion);
        }

        return mostMatchedQuestionAnswer;
    }

    private int countWords(String message, Set<String> wordsToRemove) {
        Set<String> words = new HashSet<>(Arrays.asList(message.toLowerCase().split("\\s+")));
        words.removeAll(wordsToRemove);
        return words.size();
    }

    private int countMatchingWords(String message, String question, Set<String> wordsToRemove) {
        Set<String> messageWords = new HashSet<>(Arrays.asList(message.toLowerCase().split("\\s+")));
        Set<String> questionWords = new HashSet<>(Arrays.asList(question.toLowerCase().split("\\s+")));
        messageWords.removeAll(wordsToRemove);
        questionWords.removeAll(wordsToRemove);
        messageWords.retainAll(questionWords);
        return messageWords.size();
    }

    private double calculateJaccardSimilarity(String message, String question, Set<String> wordsToRemove) {
        // Split message and question into words
        Set<String> messageWords = Arrays.stream(message.toLowerCase().split("\\s+"))
                .filter(word -> !wordsToRemove.contains(word))
                .collect(Collectors.toSet());
        Set<String> questionWords = Arrays.stream(question.toLowerCase().split("\\s+"))
                .filter(word -> !wordsToRemove.contains(word))
                .collect(Collectors.toSet());

        // Calculate intersection and union
        Set<String> intersection = new HashSet<>(messageWords);
        intersection.retainAll(questionWords);
        Set<String> union = new HashSet<>(messageWords);
        union.addAll(questionWords);

        // Calculate Jaccard similarity
        return (double) intersection.size() / union.size();
    }


}
