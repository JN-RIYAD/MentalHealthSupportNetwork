package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.Event;
import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.repositories.EventRepository;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.mhsn.riyad.controllers.EventCommentController.setupNewEventComment;

@Controller
public class EventController {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserService userService;
    @Value("${upload.base-dir}")
    private String baseUploadDir;


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
                            @RequestParam("banner") MultipartFile banner,
                            RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            model.addAttribute("error", "Login as an admin to add new event");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        try {
            // Generate a unique file name
            String fileName = UUID.randomUUID() + "_" + banner.getOriginalFilename();
            event.setBannerFileName(fileName);
            event.setBannerFileType(banner.getContentType());
            event.setUploadedDate(new Date());

            String uploadDir = baseUploadDir + "/banners";

            // Create the target directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Copy the file to the target directory
            Path targetPath = uploadPath.resolve(fileName);
            Files.copy(banner.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // Set the image URL
            String imageUrl = baseUploadDir + "banners/" + fileName;
            event.setBannerUrl(imageUrl);

            eventRepository.save(event);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store the file", e);
        }

        List<Event> eventList = eventRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("eventList", eventList);
        redirectAttributes.addFlashAttribute("eventList", eventList);
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

        Optional<Event> event = eventRepository.findById(id);

        model.addAttribute("event", event.get());
        setupNewEventComment(model);
        return "events/event-details";
    }


}
