package com.example.project.services;

import com.example.project.models.Role;
import com.example.project.models.User;
import com.example.project.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(User user) throws IllegalArgumentException {
        User registredUser = userRepository.findByLogin(user.getLogin());
        if (registredUser != null) {
            throw new IllegalArgumentException("Пользователь с данным логином уже существует");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Укажите корректный email");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            throw new IllegalArgumentException("Укажите корректный логин");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty() ||
                user.getPassword().length() < 8 || user.getPassword().length() > 20) {
            throw new IllegalArgumentException("Укажите корректный пароль (8-20 символов)");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList(new Role(1L, "USER")));
        userRepository.save(user);
    }
}
