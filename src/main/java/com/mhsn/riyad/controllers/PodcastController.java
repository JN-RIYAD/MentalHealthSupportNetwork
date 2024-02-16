package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.Podcast;
import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.repositories.PodcastRepository;
import com.mhsn.riyad.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Controller
public class PodcastController {

    @Autowired
    private PodcastRepository podcastRepository;
    @Autowired
    private UserService userService;
    @Value("${upload.base-dir}")
    private String baseUploadDir;

    @GetMapping("/show-podcast-list")
    public String showPodcastList(Model model, HttpSession httpSession) {

        User user = (User) httpSession.getAttribute("user");
        if (user != null) {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        List<Podcast> podcastList = podcastRepository.findAll();
        model.addAttribute("podcastList", podcastList);
        return "podcasts/podcast-list";
    }

    @GetMapping("/show-add-podcast-page")
    public String showAddPodcastPage(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login as an admin to save podcast");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        Podcast podcast = new Podcast();
        model.addAttribute("podcast", podcast);
        return "podcasts/add-podcast";
    }

    @PostMapping("/podcast-save")
    public String podcastSave(Model model, HttpSession httpSession,
                              @ModelAttribute Podcast podcast,
                              @RequestParam("file") MultipartFile file,
                              RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login as an admin to save podcast");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        try {
            // Generate a unique file name
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            podcast.setPodcastFileName(fileName);
            podcast.setPodcastFileType(file.getContentType());

            podcast.setUploadedDate(new Date());

            podcastRepository.save(podcast);

            String uploadDir = baseUploadDir + "/podcasts";

            // Create the target directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Copy the file to the target directory
            Path targetPath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store the file", e);
        }

        List<Podcast> podcastList = podcastRepository.findAll();
        model.addAttribute("podcastList", podcastList);
        redirectAttributes.addFlashAttribute("podcastList", podcastList);
        return "redirect:/show-podcast-list";
    }

    @GetMapping("/podcasts/{podcastFileName}")
    public void servePodcast(@PathVariable String podcastFileName, HttpServletResponse response) {
        // Determine the subdirectory based on the file type (podcasts, podcasts, etc.)
        String subdirectory = "podcasts";  // Change this to "podcasts" if needed

        // Retrieve the podcast file and write it to the response
        String podcastFilePath = baseUploadDir + subdirectory + "/" + podcastFileName;
        File podcastFile = new File(podcastFilePath);

        // Set the content type
        response.setContentType("audio/mp3");

        // Use try-with-resources to ensure closing of the input stream
        try (InputStream inputStream = new FileInputStream(podcastFile)) {
            // Copy the file content to the response
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            // Handle exception
        }
    }

    @GetMapping("/podcast-delete")
    public String deletePodcast(Model model, HttpSession httpSession, @RequestParam Long id, RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login as an admin to delete podcast");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }

        // Retrieve the podcast entity from the repository
        Podcast podcast = podcastRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid podcast id: " + id));

        // Specify the directory where the file is stored
        String uploadDir = baseUploadDir + "/podcasts";
        Path filePath = Paths.get(uploadDir).resolve(podcast.getPodcastFileName());
        try {
            // Delete the file from the directory
            Files.deleteIfExists(filePath);

            // Delete the podcast entity from the repository
            podcastRepository.deleteById(id);

        } catch (IOException e) {
            throw new RuntimeException("Failed to delete the file", e);
        }
        List<Podcast> podcastList = podcastRepository.findAll();
        model.addAttribute("podcastList", podcastList);
        redirectAttributes.addFlashAttribute("podcastList", podcastList);
        return "redirect:/show-podcast-list";
    }
}
