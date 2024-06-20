package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.entities.Video;
import com.mhsn.riyad.repositories.VideoRepository;
import com.mhsn.riyad.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
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
public class VideoController {

    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private UserService userService;
    @Value("${upload.base-dir}")
    private String baseUploadDir;

    @GetMapping("/show-video-list")
    public String showVideoList(Model model, HttpSession httpSession) {

        User user = (User) httpSession.getAttribute("user");
        if (user != null) {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        List<Video> videoList = videoRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("videoList", videoList);
        return "videos/video-list";
    }

    @GetMapping("/show-add-video-page")
    public String showAddVideoPage(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login as an admin to save video");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        Video video = new Video();
        model.addAttribute("video", video);
        return "videos/add-video";
    }

    @PostMapping("/video-save")
    public String videoSave(Model model, HttpSession httpSession,
                            @ModelAttribute Video video,
                            @RequestParam("file") MultipartFile file,
                            RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login as an admin to save video");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }

        try {
            // Generate a unique file name
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            video.setVideoFileName(fileName);
            video.setVideoFileType(file.getContentType());

            // Set other video attributes
            video.setUploadedDate(new Date());

            // Save the video entity with the generated file name
            videoRepository.save(video);

            // Specify the directory where you want to save the file
            String uploadDir = baseUploadDir + "/videos";

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

        redirectAttributes.addFlashAttribute("success", "Video saved successfully");
        return "redirect:/show-video-list";
    }

    @GetMapping("/videos/{videoFileName}")
    public void serveVideo(@PathVariable String videoFileName, HttpServletResponse response) {
        // Determine the subdirectory based on the file type (videos, podcasts, etc.)
        String subdirectory = "videos";  // Change this to "podcasts" if needed

        // Retrieve the video file and write it to the response
        String videoFilePath = baseUploadDir + subdirectory + "/" + videoFileName;
        File videoFile = new File(videoFilePath);

        // Set the content type
        response.setContentType("video/mp4");

        // Use try-with-resources to ensure closing of the input stream
        try (InputStream inputStream = new FileInputStream(videoFile)) {
            // Copy the file content to the response
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            // Handle exception
        }
    }

    @GetMapping("/video-delete")
    public String deleteVideo(Model model, HttpSession httpSession, @RequestParam Long id, RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login as an admin to delete video");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }

        // Retrieve the video entity from the repository
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid video id: " + id));

        // Specify the directory where the file is stored
        String uploadDir = baseUploadDir + "/videos";
        Path filePath = Paths.get(uploadDir).resolve(video.getVideoFileName());
        try {
            // Delete the file from the directory
            Files.deleteIfExists(filePath);

            // Delete the video entity from the repository
            videoRepository.deleteById(id);

        } catch (IOException e) {
            throw new RuntimeException("Failed to delete the file", e);
        }
        redirectAttributes.addFlashAttribute("success", "Video deleted successfully");

        return "redirect:/show-video-list";
    }

}
