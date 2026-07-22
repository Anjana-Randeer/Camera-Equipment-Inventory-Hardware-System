package com.camrent.controller;

import com.camrent.entity.Role;
import com.camrent.entity.Staff;
import com.camrent.entity.User;
import com.camrent.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("staff", new Staff());
        return "login"; // Unified Thymeleaf view
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, 
                        @RequestParam String password, 
                        HttpSession session, 
                        Model model) {
        try {
            User user = authService.authenticate(email, password);
            session.setAttribute("loggedInUser", user);
            if (user.getRole() == Role.ADMIN) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/staff/dashboard";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Invalid Credentials");
            return "login";
        }
    }

    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("staff", new Staff());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute Staff staff) {
        authService.registerStaff(staff);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
