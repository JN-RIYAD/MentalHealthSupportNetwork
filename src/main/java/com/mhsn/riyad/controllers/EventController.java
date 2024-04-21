package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.Event;
import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.repositories.EventRepository;
import com.mhsn.riyad.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class EventController {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/show-event-list")
    public String showEventList(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to see event list");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        List<Event> eventList = eventRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("eventList", eventList);
        return "events/event-list";
    }
}
