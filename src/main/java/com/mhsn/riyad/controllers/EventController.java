package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.Event;
import com.mhsn.riyad.entities.Participant;
import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.repositories.EventRepository;
import com.mhsn.riyad.repositories.ParticipantRepository;
import com.mhsn.riyad.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mhsn.riyad.controllers.EventCommentController.setupNewEventComment;

@Controller
public class EventController {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserService userService;
    @Value("${upload.base-dir}")
    private String baseUploadDir;
    @Autowired
    private ParticipantRepository participantRepository;


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

    @GetMapping("/show-update-event-page")
    public String showUpdateEventPage(Model model, HttpSession httpSession, @RequestParam Long id) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to access events");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        Event eventToUpdate = eventRepository.findById(id).get();
        model.addAttribute("eventToUpdate", eventToUpdate);
        return "events/update-event";
    }

    @GetMapping("/show-add-event-page")
    public String showAddEventPage(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to add new event");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        Event event = new Event();
        model.addAttribute("event", event);
        return "events/add-event";
    }

    @PostMapping("/event-save")
    public String eventSave(Model model, HttpSession httpSession,
                            @ModelAttribute Event event,
                            RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to add new event");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        event.setEventStatus("Active");
        event.setNumberOfParticipants(0);
        event.setCommentList(new ArrayList<>());
        event.setParticipantList(new ArrayList<>());

        eventRepository.save(event);

        redirectAttributes.addFlashAttribute("success", "Event created successfully.");
        return "redirect:/show-event-list";
    }

    @PostMapping("/event-update")
    public String eventUpdate(Model model, HttpSession httpSession, @ModelAttribute Event eventToUpdate, RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to access events");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }

        Event savedEvent = eventRepository.findById(eventToUpdate.getId()).get();

        savedEvent.setTitle(eventToUpdate.getTitle());
        savedEvent.setDescription(eventToUpdate.getDescription());
        savedEvent.setLocation(eventToUpdate.getLocation());
        savedEvent.setDateAndTime(eventToUpdate.getDateAndTime());
        savedEvent.setOrganizer(eventToUpdate.getOrganizer());
        savedEvent.setSpeaker(eventToUpdate.getSpeaker());
        savedEvent.setSpeakerDesignation(eventToUpdate.getSpeakerDesignation());
        savedEvent.setChiefGuest(eventToUpdate.getChiefGuest());
        savedEvent.setChiefGuestDesignation(eventToUpdate.getChiefGuestDesignation());
        savedEvent.setEventStatus(eventToUpdate.getEventStatus());

        eventRepository.save(savedEvent);

        redirectAttributes.addFlashAttribute("success", "Event updated successfully.");

        return "redirect:/show-event-list";
    }


    @GetMapping("/show-event-details")
    public String showEventDetails(Model model, HttpSession httpSession, @RequestParam Long id) {

        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login to view event details");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }

        Event event = eventRepository.findById(id).get();
        model.addAttribute("event", event);

        Optional<Participant> participant = participantRepository.findByUserAndEvent(user, event);

        if (participant.isPresent())
            model.addAttribute("isGoing", "yes");
        else
            model.addAttribute("isGoing", "no");

        model.addAttribute("totalParticipants", event.getParticipantList().size());

        setupNewEventComment(model);

        return "events/event-details";
    }

    @GetMapping("/event-going")
    public String eventGoing(Model model, HttpSession httpSession, RedirectAttributes redirectAttributes, @RequestParam Long eventId) {

        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login to go/cancel event");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }

        Event savedEvent = eventRepository.findById(eventId).get();

        Participant participant = new Participant();
        participant.setEvent(savedEvent);
        participant.setUser(user);
        savedEvent.getParticipantList().add(participant);

        eventRepository.save(savedEvent);

        redirectAttributes.addFlashAttribute("success", "You are going to this event");
        redirectAttributes.addAttribute("id", eventId);
        return "redirect:/show-event-details";
    }

    @GetMapping("/event-not-going")
    public String eventNotGoing(Model model, HttpSession httpSession, RedirectAttributes redirectAttributes, @RequestParam Long eventId) {

        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login to go/cancel event");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }

        Event savedEvent = eventRepository.findById(eventId).get();
        Participant participant = participantRepository.findByUserAndEvent(user, savedEvent).get();
        savedEvent.getParticipantList().remove(participant);

        eventRepository.save(savedEvent);

        redirectAttributes.addFlashAttribute("success", "You are not going to this event");
        redirectAttributes.addAttribute("id", eventId);
        return "redirect:/show-event-details";
    }

    @GetMapping("/inactivate-event")
    public String inactivateEvent(Model model, HttpSession httpSession, RedirectAttributes redirectAttributes, @RequestParam Long id) {

        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login to inactivate event");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }

        Event event = eventRepository.findById(id).get();
        event.setEventStatus("Inactive");
        eventRepository.save(event);
        redirectAttributes.addFlashAttribute("success", "Event Inactivated successfully.");
        return "redirect:/show-event-list";
    }

    @GetMapping("/event-delete")
    public String eventDelete(Model model, HttpSession httpSession, @RequestParam Long id, RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to delete event");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        eventRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Event deleted successfully");
        return "redirect:/show-event-list";
    }

}
