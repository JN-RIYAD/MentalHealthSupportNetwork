package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.Event;
import com.mhsn.riyad.entities.EventComment;
import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.repositories.EventCommentRepository;
import com.mhsn.riyad.repositories.EventRepository;
import com.mhsn.riyad.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EventCommentController {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventCommentRepository commentRepository;
    @Autowired
    private UserService userService;

    public static void setupNewEventComment(Model model) {
        EventComment newComment = new EventComment();
        model.addAttribute("newComment", newComment);
    }

    @PostMapping("/event-comment-save")
    public String eventCommentSave(Model model, HttpSession httpSession, RedirectAttributes redirectAttributes, @ModelAttribute EventComment comment, @RequestParam Long eventId) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to access group events");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        Event event = eventRepository.findById(eventId).get();
        comment.setCommentDateTime(LocalDateTime.now());
        comment.setEvent(event);
        comment.setUser(user);


        if (event.getCommentList() == null) {
            List<EventComment> commentList = new ArrayList<>();
            commentList.add(comment);
            event.setCommentList(commentList);
        } else {
            event.getCommentList().add(comment);
        }
        eventRepository.save(event);

        redirectAttributes.addFlashAttribute("success", "Comment saved successfully.");
        redirectAttributes.addAttribute("id", eventId);
        return "redirect:/show-event-details";
    }

    @GetMapping("/show-update-event-comment-page")
    public String showUpdateEventPage(Model model, HttpSession httpSession, @RequestParam Long commentId, @RequestParam Long eventId) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to access group events");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        EventComment commentToUpdate = commentRepository.findById(commentId).get();

        Event event = eventRepository.findById(eventId).get();

        model.addAttribute("event", event);
        model.addAttribute("commentToUpdate", commentToUpdate);

        return "events/update-comment";
    }

    @PostMapping("/event-comment-update")
    public String commentUpdate(Model model, HttpSession httpSession, RedirectAttributes redirectAttributes, @ModelAttribute EventComment comment, @RequestParam Long eventId) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to access group events");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        Event event = eventRepository.findById(eventId).get();

        comment.setUpdatedDateTime(LocalDateTime.now());
        comment.setEvent(event);
        comment.setUser(user);

        commentRepository.save(comment);

        redirectAttributes.addFlashAttribute("success", "Comment updated successfully.");
        redirectAttributes.addAttribute("id", eventId);
        return "redirect:/show-event-details";

    }

    @GetMapping("/event-comment-delete")
    public String eventDelete(Model model, HttpSession httpSession, RedirectAttributes redirectAttributes, @RequestParam Long commentId, @RequestParam Long eventId) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to access group events");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        commentRepository.deleteById(commentId);
        redirectAttributes.addFlashAttribute("success", "Comment deleted successfully.");
        redirectAttributes.addAttribute("id", eventId);
        return "redirect:/show-event-details";
    }
}
