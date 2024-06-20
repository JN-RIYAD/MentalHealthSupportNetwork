package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.Discussion;
import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.repositories.DiscussionRepository;
import com.mhsn.riyad.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

import static com.mhsn.riyad.controllers.DiscussionCommentController.setupNewDiscussionComment;

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
        List<Discussion> discussionList = discussionRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
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

        setupNewDiscussionComment(model);

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
        Discussion discussionToUpdate = discussionRepository.findById(id).get();
        model.addAttribute("discussionToUpdate", discussionToUpdate);
        return "discussions/update-discussion";
    }

    @PostMapping("/discussion-save")
    public String discussionSave(Model model, HttpSession httpSession, @ModelAttribute Discussion discussion, RedirectAttributes redirectAttributes) {
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

        redirectAttributes.addFlashAttribute("success", "Group discussion saved successfully.");
        return "redirect:/show-discussion-list";
    }

    @PostMapping("/discussion-update")
    public String discussionUpdate(Model model, HttpSession httpSession, @ModelAttribute Discussion discussionToUpdate, RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to access group discussions");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }

        Discussion savedDiscussion = discussionRepository.findById(discussionToUpdate.getId()).get();
        savedDiscussion.setUpdatedDate(new Date());
        savedDiscussion.setQueryTopic(discussionToUpdate.getQueryTopic());
        savedDiscussion.setInquirerName(discussionToUpdate.getInquirerName());
        savedDiscussion.setInquirerGender(discussionToUpdate.getInquirerGender());
        savedDiscussion.setQueryDescription(discussionToUpdate.getQueryDescription());

        discussionRepository.save(savedDiscussion);

        redirectAttributes.addFlashAttribute("success", "Group discussion updated successfully.");

        return "redirect:/show-discussion-list";
    }

    @GetMapping("/discussion-delete")
    public String discussionDelete(Model model, HttpSession httpSession, @RequestParam Long id, RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to access group discussions");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        discussionRepository.deleteById(id);

        redirectAttributes.addFlashAttribute("success", "Group discussion deleted successfully.");

        return "redirect:/show-discussion-list";
    }
}
