package com.github.juanmanuel.nottodaytomorrow.controllers;

import com.github.juanmanuel.nottodaytomorrow.models.Team;
import com.github.juanmanuel.nottodaytomorrow.services.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team")
public class TeamController {
    @Autowired
    private TeamService teamService;

    @Operation(summary = "Crea un equipo")
    @PostMapping
    public Team create(@RequestBody Team team) {
        return teamService.create(team);
    }

    @Operation(summary = "Actualiza un equipo")
    @PutMapping("/{id}")
    public Team update(@PathVariable Long id, @RequestBody Team team) {
        return teamService.update(id, team);
    }

    @Operation(summary = "Elimina un equipo")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return teamService.delete(id);
    }

    @Operation(summary = "Busca un equipo por su id")
    @GetMapping("/{id}")
    public Team getById(@PathVariable Long id) {
        return teamService.getById(id);
    }

    @Operation(summary = "Busca todos los equipos")
    @GetMapping
    public List<Team> getAll() {
        return teamService.getAll();
    }
}
