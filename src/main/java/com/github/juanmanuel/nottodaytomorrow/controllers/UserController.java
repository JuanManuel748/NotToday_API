package com.github.juanmanuel.nottodaytomorrow.controllers;

import com.github.juanmanuel.nottodaytomorrow.models.Team;
import com.github.juanmanuel.nottodaytomorrow.models.User;
import com.github.juanmanuel.nottodaytomorrow.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Crea un usuario")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Operation(summary = "Actualiza un usuario")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User userUpdated = userService.update(id, updatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(userUpdated);
    }

    @Operation(summary = "Eliminar un usuario por su id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Buscar un usuario por su id")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Operation(summary = "Buscar todos los usuarios")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @Operation(summary = "Buscar usuarios por su nombre")
    @GetMapping("/name/{name}")
    public ResponseEntity<List<User>> getUserByName(@PathVariable String name) {
        List<User> user = userService.getByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Operation(summary = "Buscar usuarios por su email")
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Operation(summary = "Buscar equipos del usuario por su email")
    @GetMapping("/email/{email}/teams")
    public ResponseEntity<List<Team>> getUserTeamsByEmail(@PathVariable String email) {
        List<Team> teams = userService.getTeamsByUserEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(teams);
        // DA ERROR 500
    }

    @Operation(summary = "Añadir equipo al usuario por su id")
    @PostMapping("/{userId}/teams/{teamId}")
    public ResponseEntity<User> addTeamToUser(@PathVariable Long userId, @PathVariable Long teamId, @RequestParam String role) {
        User user = userService.addTeamToUser(userId, teamId, role);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

}
