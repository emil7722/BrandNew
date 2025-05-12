package com.example.demo.Controller;

import com.example.demo.Model.User;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpSession session, Model model) {
        System.out.println("✔️ Login success. Logged in user: " + session.getAttribute("loggedInUser"));
        if (userService.login(user.getEmail(), user.getPassword())) {
            session.setAttribute("loggedInUser", user.getEmail());
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                           @RequestParam String confirmPassword,
                           Model model) {
        String password = user.getPassword();

        // Basic strength check: min 8 chars, 1 digit, 1 uppercase
        if (!password.matches("^(?=.*[A-Z])(?=.*\\d).{8,}$")) {
            model.addAttribute("error", "Password must be at least 8 characters, include an uppercase letter and a number.");
            return "register";
        }

        if (userService.register(user, confirmPassword)) {
            return "redirect:/login";
        } else {
            model.addAttribute("error", "Registration failed: email may be taken or passwords do not match.");
            return "register";
        }
    }
}

