package com.example.demo.Service;

import com.example.demo.Model.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final Map<String, User> users = new HashMap<>();

    public boolean register(User user, String confirmPassword) {
        if (users.containsKey(user.getEmail())) return false;
        if (!user.getPassword().equals(confirmPassword)) return false;

        users.put(user.getEmail(), user);
        return true;
    }

    public boolean login(String email, String password) {
        User user = users.get(email);
        return user != null && user.getPassword().equals(password);
    }

    public User findByEmail(String email) {
        return users.get(email);
    }
}
