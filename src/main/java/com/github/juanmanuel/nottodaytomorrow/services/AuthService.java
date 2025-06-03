package com.github.juanmanuel.nottodaytomorrow.services;

import com.github.juanmanuel.nottodaytomorrow.exceptions.AuthException;
import com.github.juanmanuel.nottodaytomorrow.exceptions.NotFoundException;
import com.github.juanmanuel.nottodaytomorrow.models.User;
import com.github.juanmanuel.nottodaytomorrow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passEncoder;

    public User authenticate(String key, String pass) {
        if (key == null || key.isEmpty() || pass == null || pass.isEmpty()) {
            throw new NotFoundException("Key or password cannot be empty.", User.class);
        }
        Optional<User> userOptional = userRepository.findByEmail(key);
        if (userOptional.isEmpty()) {
            List<User> usrLst = userRepository.findByName(key);
            if (!usrLst.isEmpty()) {
                userOptional = Optional.of(usrLst.getFirst());
            }
        }
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User not found.", User.class);
        }
        User user = userOptional.get();

        if (passEncoder.matches(pass, user.getPassword())) {
            return user;
        } else {
            throw new AuthException("Invalid password.");
        }
    }
}
