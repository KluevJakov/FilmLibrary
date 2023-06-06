package com.example.project.controller;

import com.example.project.models.User;
import com.example.project.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AuthController {
    @Autowired
    AuthService authService;

    @GetMapping(path = "reg")
    public String registerPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "reg";
    }

    @PostMapping(path = "reg")
    public String register(Model model, @ModelAttribute("user") User user) {
        String errorMessage = null;
        try {
            authService.register(user);
            return "redirect:/log";
        } catch (Exception e) {
            errorMessage = e.getMessage();
            model.addAttribute("errorMessage", errorMessage);
            return "reg";
        }
    }

    @GetMapping(path = "log")
    public String loginPage(Model model, @RequestParam(required = false) boolean error) {
        User user = new User();
        if (error) {
            model.addAttribute("errorMessage", "Проверьте данные для входа");
        }
        model.addAttribute("user", user);
        return "log";
    }
}
