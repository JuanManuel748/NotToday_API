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
        System.out.println("\nCreating task: " + task + "\n\n");
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


    @Operation(summary = "Busca tareas por su usuario asignado")
    @GetMapping("/assigned/{assignedId}/all")
    public ResponseEntity<List<Task>> getByAssigned(@PathVariable Long assignedId) {
        List<Task> tasks = taskService.findByAssigned(assignedId);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @Operation(summary = "Busca tareas no completadas por su usuario asignado")
    @GetMapping("/assigned/{assignedId}")
    public ResponseEntity<List<Task>> getByAssignedNotComp(@PathVariable Long assignedId) {
        List<Task> tasks = taskService.findByAssignedNotComp(assignedId);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @Operation(summary = "Busca tareas por su equipo")
    @GetMapping("/team/{teamId}/all")
    public ResponseEntity<List<Task>> getByTeam(@PathVariable Long teamId) {
        List<Task> tasks = taskService.findByTeam(teamId);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @Operation(summary = "Busca tareas sin completar por su equipo")
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<Task>> getByTeamNotCompleted(@PathVariable Long teamId) {
        List<Task> tasks = taskService.findByTeamNotCompleted(teamId);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @Operation(summary = "Busca tareas por su estado")
    @GetMapping("/team/{teamId}/state/{state}")
    public ResponseEntity<List<Task>> getByState(@PathVariable Long teamId, @PathVariable String state) {
        List<Task> tasks = taskService.findByStatus(teamId, state);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }



}
