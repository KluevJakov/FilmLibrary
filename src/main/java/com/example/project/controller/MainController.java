package com.example.project.controller;

import com.example.project.services.FilmService;
import com.example.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private FilmService filmService;

    @GetMapping(path = "/")
    public String index() {
        return "index";
    }

    @GetMapping(path = "user")
    public String user(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user", userService.userByEmail(((User) authentication.getPrincipal()).getUsername()));
        return "user";
    }

    @GetMapping(path = "accessDenied")
    public String accessDenied() {
        return "accessDenied";
    }

    @GetMapping(path = "admin")
    public String admin() {
        return "admin";
    }
}
