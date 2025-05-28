package com.github.juanmanuel.nottodaytomorrow.controllers;

import com.github.juanmanuel.nottodaytomorrow.models.Team;
import com.github.juanmanuel.nottodaytomorrow.models.User;
import com.github.juanmanuel.nottodaytomorrow.services.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {
    @Autowired
    private TeamService teamService;

    @Operation(summary = "Crea un equipo")
    @PostMapping
    public ResponseEntity<Team> create(@RequestBody Team team) {
        Team t = teamService.create(team);
        return ResponseEntity.status(HttpStatus.CREATED).body(t);
    }

    @Operation(summary = "Actualiza un equipo")
    @PutMapping("/{id}")
    public ResponseEntity<Team> update(@PathVariable Long id, @RequestBody Team team) {
        Team t = teamService.update(id, team);
        return ResponseEntity.status(HttpStatus.OK).body(t);    }

    @Operation(summary = "Elimina un equipo")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (teamService.delete(id)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Busca un equipo por su id")
    @GetMapping("/{id}")
    public ResponseEntity<Team> getById(@PathVariable Long id) {
        Team t = teamService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(t);
    }

    @Operation(summary = "Busca todos los equipos")
    @GetMapping
    public ResponseEntity<List<Team>> getAll() {
        List<Team> teams = teamService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(teams);
    }


    @Operation(summary = "Buscar el equipo por su código")
    @GetMapping("/code/{code}")
    public ResponseEntity<Team> getByCode(@PathVariable String code) {
        Team t = teamService.getByCode(code);
        return ResponseEntity.status(HttpStatus.OK).body(t);
    }

    @Operation(summary = "Buscar usuarios del equipo por su id")
    @GetMapping("/{id}/users")
    public ResponseEntity<List<User>> getUsersByTeamId(@PathVariable Long id) {
        List<User> users = teamService.getUsersByTeamId(id);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }



}
