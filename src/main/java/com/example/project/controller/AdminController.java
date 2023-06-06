package com.example.project.controller;

import com.example.project.models.Film;
import com.example.project.models.User;
import com.example.project.models.dto.FilmDto;
import com.example.project.services.FilmService;
import com.example.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private FilmService filmService;

    @GetMapping("/users")
    public List<User> users(@RequestParam(required = false) String search) {
        return userService.getUsers(search);
    }

    @GetMapping("/films")
    public List<Film> films(@RequestParam(required = false) String search) {
        return filmService.getFilms(search);
    }

    @PostMapping("/addFilm")
    public void addFilm(@RequestBody Film film) {
        filmService.addFilm(film);
    }

    @GetMapping("/film")
    public Film film (@RequestParam Long id){
        return filmService.filmById(id);
    }

    @GetMapping("/userById")
    public User userById (@RequestParam Long id){
        return userService.userById(id);
    }

    @DeleteMapping("/removeFilm")
    public void removeFilm (@RequestParam Long id){
        filmService.removeFilm(id);
    }

    @DeleteMapping("/removeUser")
    public void removeUser (@RequestParam Long id){
        userService.removeUser(id);
    }

    @PutMapping("/editUser")
    public void editUser(@RequestBody User user) {
        userService.editUser(user);
    }

    @GetMapping("/recs")
    public List<Film> recs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.userByEmail(((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername());
        return filmService.recs(user);
    }

    @GetMapping("/filmExt")
    public FilmDto filmExt (@RequestParam Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.userByEmail(((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername());
        return filmService.filmDtoById(id, user.getId());
    }

    @GetMapping("/action")
    public void action(@RequestParam Long id, @RequestParam Long action) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.userByEmail(((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername());
        filmService.processAction(id, action, user.getId());
    }
}
