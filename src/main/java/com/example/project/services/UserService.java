package com.example.project.services;

import com.example.project.models.User;
import com.example.project.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LikedRepository likedRepository;
    @Autowired
    private UnlikedRepository unlikedRepository;
    @Autowired
    private ThrownRepository thrownRepository;
    @Autowired
    private ViewedRepository viewedRepository;

    public List<User> getUsers(String search) {
        if (search != null && !search.isEmpty()) {
            return userRepository.findUsersSearch(search.toLowerCase());
        } else {
            return userRepository.findUsers();
        }
    }

    public void removeUser(Long id) {
        likedRepository.removeByUserId(id);
        unlikedRepository.removeByUserId(id);
        viewedRepository.removeByUserId(id);
        thrownRepository.removeByUserId(id);
        userRepository.deleteById(id);
    }

    public User userById(Long id) {
        User userWithoutPassword = userRepository.findById(id).get();
        //userWithoutPassword.setPassword(""); пароль всё равно в зашифрованном виде
        return userWithoutPassword;
    }

    public void editUser(User user) {
        User enrichUser = userRepository.findById(user.getId()).get();
        enrichUser.setLogin(user.getLogin());
        enrichUser.setEmail(user.getEmail());
        userRepository.save(enrichUser);
    }

    public User userByEmail(String email) {
        User userWithoutPassword = userRepository.findByEmail(email);
        //userWithoutPassword.setPassword("");
        return userWithoutPassword;
    }
}
