package com.github.juanmanuel.nottodaytomorrow.services;

import com.github.juanmanuel.nottodaytomorrow.exceptions.NotFoundException;
import com.github.juanmanuel.nottodaytomorrow.models.Task;
import com.github.juanmanuel.nottodaytomorrow.models.Team;
import com.github.juanmanuel.nottodaytomorrow.repositories.TaskRepository;
import com.github.juanmanuel.nottodaytomorrow.repositories.TeamRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;


    public List<Task> getAll() throws NotFoundException {
        List<Task> tasks = taskRepository.findAll();
        if (!tasks.isEmpty()) {
            return tasks;
        } else {
            throw new NotFoundException("No tasks found", Task.class);
        }
    }

    public Task getById(Long id) throws NotFoundException {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            return task.get();
        } else {
            throw new NotFoundException("Task not found with id: " + id, Task.class);
        }
    }

    public Task create(Task task) {
        return taskRepository.save(task);
    }

    public Task update(Long id, Task task) throws NotFoundException {
        Optional<Task> existing = taskRepository.findById(id);
        if (existing.isPresent()) {
            Task updatedTask = existing.get();
            updatedTask.setName(task.getName());
            updatedTask.setDescription(task.getDescription());
            updatedTask.setLimitDate(task.getLimitDate());
            updatedTask.setState(task.getState());
            updatedTask.setTeam(task.getTeam());
            updatedTask.setCreator(task.getCreator());
            updatedTask.setAssigned(task.getAssigned());
            return taskRepository.save(updatedTask);
        } else {
            throw new NotFoundException("Task not found with id: " + id, Task.class);
        }
    }

    public boolean delete(Long id) throws NotFoundException {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            taskRepository.delete(task.get());
            return true;
        } else {
            throw new NotFoundException("Task not found with id: " + id, Task.class);
        }
    }

    public List<Task> findByAssigned(Long assignedId) throws NotFoundException {
        List<Task> tasks = taskRepository.findByAssigned(assignedId);
        if (!tasks.isEmpty()) {
            return tasks;
        } else {
            throw new NotFoundException("Task not found with assigned id: " + assignedId, tasks);
        }
    }

    public List<Task> findByAssignedNotComp(Long assignedId) throws NotFoundException {
        List<Task> tasks = taskRepository.findByAssignedNotComp(assignedId);
        if (!tasks.isEmpty()) {
            return tasks;
        } else {
            throw new NotFoundException("Task not found with assigned id: " + assignedId, tasks);
        }
    }

    public List<Task> findByTeam(Long teamId) throws NotFoundException {
        List<Task> tasks = taskRepository.findByTeam(teamId);
        if (!tasks.isEmpty()) {
            return tasks;
        } else {
            throw new NotFoundException("Task not found with team id: " + teamId, tasks);
        }
    }

    public List<Task> findByTeamNotCompleted(Long teamId) throws NotFoundException {
        List<Task> tasks = taskRepository.findByTeamNotCompleted(teamId);
        if (!tasks.isEmpty()) {
            return tasks;
        } else {
            throw new NotFoundException("Task not found with team id: " + teamId, tasks);
        }
    }

    public List<Task> findByStatus(Long teamId, String status) throws NotFoundException {
        List<Task> tasks = taskRepository.findByStatus(teamId, status);
        if (!tasks.isEmpty()) {
            return tasks;
        } else {
            throw new NotFoundException("Task not found with status: " + status, tasks);
        }
    }

    public Long countCompletedTasksByAssignedAndTeam(Long teamId, Long userId) {
        return taskRepository.countCompletedTasksByAssignedNteam(teamId, userId);
    }


    /**
     * Método programado para actualizar tareas pendientes a atrasadas.
     * Se ejecuta cada hora (puedes ajustar la expresión cron según tus necesidades).
     * Ejemplo: "0 0 0 * * ?" se ejecuta a medianoche todos los días.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateOverdueTasks() {
        LocalDateTime now = LocalDateTime.now();
        List<Task> overdueTasks = taskRepository.findTasksByStateAndLimitDateBefore(now);

        if (!overdueTasks.isEmpty()) {
            for (Task task : overdueTasks) {
                task.setState("ATRASADA");
                taskRepository.save(task);
            }
        }
    }



}
