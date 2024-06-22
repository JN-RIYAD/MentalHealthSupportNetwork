package com.mhsn.riyad.controllers;

import com.mhsn.riyad.entities.PaymentHistory;
import com.mhsn.riyad.entities.User;
import com.mhsn.riyad.repositories.PaymentHistoryRepository;
import com.mhsn.riyad.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PaymentController {

    @Autowired
    private UserService userService;
    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    @GetMapping("/initiate-payment")
    public String initiatePayment(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("user")) {
            model.addAttribute("error", "Login first as a user to payment");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        PaymentHistory paymentHistory = new PaymentHistory();
        model.addAttribute("paymentHistory", paymentHistory);
        return "payments/add-balance-request";
    }

    @PostMapping("/payment-history-save")
    public String paymentHistorySave(Model model, HttpSession httpSession,
                                     @ModelAttribute PaymentHistory paymentHistory,
                                     RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null || !user.getRole().equals("user")) {
            model.addAttribute("error", "Login as a user to submit balance adding request");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        paymentHistory.setPaymentStatus("Waiting for Approval");
        paymentHistory.setRequestDateAndTime(LocalDateTime.now());
        paymentHistory.setUser(user);
        paymentHistoryRepository.save(paymentHistory);

        redirectAttributes.addFlashAttribute("success", "Balance adding request submitted successfully. Please wait for admin's approval");
        return "redirect:/show-payment-histories";
    }

    @GetMapping("/show-payment-histories")
    public String showsPaymentHistories(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Login first to view payment history");
            return "login";
        } else {
            userService.setRoleInModelAndHttpSession(httpSession, model, user);
        }
        List<PaymentHistory> paymentHistories = new ArrayList<>();
        if (user.getRole().equals("admin"))
            paymentHistories = paymentHistoryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        else
            paymentHistories = paymentHistoryRepository.findByUser(user, Sort.by(Sort.Direction.DESC, "id"));

        model.addAttribute("paymentHistories", paymentHistories);
        return "payments/payment-histories";
    }
}
