package com.example.demo.Service;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public boolean register(User user, String confirmPassword) {
        if (!user.getPassword().equals(confirmPassword)) return false;
        if (userRepo.findByEmail(user.getEmail()).isPresent()) return false;

        userRepo.save(user);
        return true;
    }

    public boolean login(String email, String password) {
        return userRepo.findByEmail(email)
                .map(u -> u.getPassword().equals(password))
                .orElse(false);
    }

    public Optional<User> getByEmail(String email) {
        return userRepo.findByEmail(email);
    }
}