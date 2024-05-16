package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.Discussion;
import com.mhsn.riyad.entities.DiscussionComment;
import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.repositories.DiscussionCommentRepository;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class DiscussionCommentController {
    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private DiscussionCommentRepository commentRepository;
    @Autowired
    private UserService userService;

    public static void setupNewDiscussionComment(Model model) {
        DiscussionComment newComment = new DiscussionComment();
        model.addAttribute("newComment", newComment);
    }

    @PostMapping("/discussion-comment-save")
    public String discussionCommentSave(Model model, HttpSession httpSession, @ModelAttribute DiscussionComment comment, @RequestParam Long discussionId) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to access group discussions");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        Discussion discussion = discussionRepository.findById(discussionId).get();
        comment.setCommentDate(new Date());
        comment.setDiscussion(discussion);
        comment.setUser(user);


        if (discussion.getCommentList() == null) {
            List<DiscussionComment> commentList = new ArrayList<>();
            commentList.add(comment);
            discussion.setCommentList(commentList);
        } else {
            discussion.getCommentList().add(comment);
        }
        discussionRepository.save(discussion);

        Discussion newDiscussionWithComment = discussionRepository.findById(discussionId).get();
        model.addAttribute("discussion", newDiscussionWithComment);

        setupNewDiscussionComment(model);

        return "discussions/discussion-details";
    }

    @GetMapping("/show-update-discussion-comment-page")
    public String showUpdateDiscussionPage(Model model, HttpSession httpSession, @RequestParam Long commentId, @RequestParam Long discussionId) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to access group discussions");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        DiscussionComment commentToUpdate = commentRepository.findById(commentId).get();

        Discussion discussion = discussionRepository.findById(discussionId).get();

        model.addAttribute("discussion", discussion);
        model.addAttribute("commentToUpdate", commentToUpdate);

        return "discussions/update-comment";
    }

    @PostMapping("/discussion-comment-update")
    public String commentUpdate(Model model, HttpSession httpSession, @ModelAttribute DiscussionComment comment, @RequestParam Long discussionId) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to access group discussions");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        Discussion discussion = discussionRepository.findById(discussionId).get();

        comment.setUpdatedDate(new Date());
        comment.setDiscussion(discussion);
        comment.setUser(user);

        commentRepository.save(comment);

        model.addAttribute("discussion", discussion);
        setupNewDiscussionComment(model);

        return "discussions/discussion-details";

    }

    @GetMapping("/discussion-comment-delete")
    public String discussionDelete(Model model, HttpSession httpSession, @RequestParam Long commentId, @RequestParam Long discussionId) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to access group discussions");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        commentRepository.deleteById(commentId);

        Discussion discussion = discussionRepository.findById(discussionId).get();
        model.addAttribute("discussion", discussion);

        setupNewDiscussionComment(model);

        return "discussions/discussion-details";
    }
}
