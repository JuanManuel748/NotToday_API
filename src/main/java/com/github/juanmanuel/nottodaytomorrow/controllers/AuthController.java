package com.github.juanmanuel.nottodaytomorrow.controllers;

import com.github.juanmanuel.nottodaytomorrow.models.User;
import com.github.juanmanuel.nottodaytomorrow.security.JwtTokenProvider;
import com.github.juanmanuel.nottodaytomorrow.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "Verifica al usuario y devuelve un token JWT")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody User user) {
        try {
            User authenticatedUser = authService.authenticate(user.getEmail(), user.getPassword());
            String token = jwtTokenProvider.generateToken(authenticatedUser);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid credentials or authentication error.");
        }
    }
}
