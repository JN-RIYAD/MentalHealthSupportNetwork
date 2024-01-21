package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.Discussion;
import com.mhsn.riyad.entities.DiscussionComment;
import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.repositories.DiscussionRepository;
import com.mhsn.riyad.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
public class GroupDiscussionController {
    @Autowired
    private DiscussionRepository discussionRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/show-discussion-list")
    public String showDiscussionList(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to access group discussions");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        List<Discussion> discussionList = discussionRepository.findAll();
        model.addAttribute("discussionList", discussionList);
        return "discussions/discussion-list";
    }

    @GetMapping("/show-discussion-details")
    public String showDiscussionDetails(Model model, HttpSession httpSession, @RequestParam Long id) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to access group discussions");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        Discussion discussion = discussionRepository.findById(id).get();
        model.addAttribute("discussion", discussion);
        DiscussionComment newComment = new DiscussionComment();
        model.addAttribute("newComment", newComment);

        return "discussions/discussion-details";
    }

    @GetMapping("/show-add-discussion-page")
    public String showAddDiscussionPage(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to access group discussions");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        Discussion discussion = new Discussion();
        model.addAttribute("discussion", discussion);
        return "discussions/add-discussion";
    }

    @GetMapping("/show-update-discussion-page")
    public String showUpdateDiscussionPage(Model model, HttpSession httpSession, @RequestParam Long id) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to access group discussions");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        Discussion discussion = discussionRepository.findById(id).get();
        model.addAttribute("discussion", discussion);
        return "discussions/update-discussion";
    }

    @PostMapping("/discussion-save")
    public String discussionSave(Model model, HttpSession httpSession, @ModelAttribute Discussion discussion) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to access group discussions");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        discussion.setQueryDate(new Date());
        discussion.setUser(user);
        discussionRepository.save(discussion);

        List<Discussion> discussionList = discussionRepository.findAll();
        model.addAttribute("discussionList", discussionList);
        return "discussions/discussion-list";
    }

    @PostMapping("/discussion-update")
    public String discussionUpdate(Model model, HttpSession httpSession, @ModelAttribute Discussion discussion) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to access group discussions");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        discussion.setUpdatedDate(new Date());

        discussionRepository.save(discussion);

        List<Discussion> discussionList = discussionRepository.findAll();
        model.addAttribute("discussionList", discussionList);
        return "discussions/discussion-list";
    }

    @GetMapping("/discussion-delete")
    public String discussionDelete(Model model, HttpSession httpSession, @RequestParam Long id) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to access group discussions");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        discussionRepository.deleteById(id);
        List<Discussion> discussionList = discussionRepository.findAll();
        model.addAttribute("discussionList", discussionList);
        return "discussions/discussion-list";
    }
}
