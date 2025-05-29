package com.github.juanmanuel.nottodaytomorrow.controllers;

import com.github.juanmanuel.nottodaytomorrow.models.Task;
import com.github.juanmanuel.nottodaytomorrow.models.Team;
import com.github.juanmanuel.nottodaytomorrow.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Operation(summary = "Crea una tarea")
    @PostMapping
    public ResponseEntity<Task> create(@RequestBody Task task) {
        Task t = taskService.create(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(t);
    }

    @Operation(summary = "Actualiza una tarea")
    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable Long id, @RequestBody Task task) {
        Task t = taskService.update(id, task);
        return ResponseEntity.status(HttpStatus.OK).body(t);    }

    @Operation(summary = "Elimina una tarea")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (taskService.delete(id)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Busca una tarea por su id")
    @GetMapping("/{id}")
    public ResponseEntity<Task> getById(@PathVariable Long id) {
        Task tasks = taskService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);

    }

    @Operation(summary = "Busca todas las tareas")
    @GetMapping
    public ResponseEntity<List<Task>> getAll() {
        List<Task> tasks =  taskService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @Operation(summary = "Busca tareas por su estado")
    @GetMapping("/state/{state}")
    public ResponseEntity<List<Task>> getByState(@PathVariable String state) {
        List<Task> tasks = taskService.findByStatus(state);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @Operation(summary = "Busca tareas por su creador")
    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<Task>> getByCreator(@PathVariable Long creatorId) {
        List<Task> tasks = taskService.findByCreator(creatorId);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @Operation(summary = "Busca tareas por su usuario asignado")
    @GetMapping("/assigned/{assignedId}")
    public ResponseEntity<List<Task>> getByAssigned(@PathVariable Long assignedId) {
        List<Task> tasks = taskService.findByAssigned(assignedId);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @Operation(summary = "Busca tareas por su equipo")
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<Task>> getByTeam(@PathVariable Long teamId) {
        List<Task> tasks = taskService.findByTeam(teamId);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @Operation(summary = "Busca tareas por rango de fecha")
    @GetMapping("/date")
    public ResponseEntity<List<Task>> getByDate(@RequestParam String startDate, @RequestParam String endDate) {
        List<Task> tasks = taskService.findByDateRange(startDate, endDate);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }


}
