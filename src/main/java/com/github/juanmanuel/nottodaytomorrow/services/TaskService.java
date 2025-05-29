package com.github.juanmanuel.nottodaytomorrow.services;

import com.github.juanmanuel.nottodaytomorrow.exceptions.NotFoundException;
import com.github.juanmanuel.nottodaytomorrow.models.Task;
import com.github.juanmanuel.nottodaytomorrow.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
            throw new RuntimeException("Task not found with id: " + id);
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
            throw new RuntimeException("Task not found with id: " + id);
        }
    }

    public boolean delete(Long id) throws NotFoundException {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            taskRepository.delete(task.get());
            return true;
        } else {
            throw new RuntimeException("Task not found with id: " + id);
        }
    }


    public List<Task> findByCreator(Long creatorId) throws NotFoundException {
        List<Task> tasks = taskRepository.findByCreator(creatorId);
        if (!tasks.isEmpty()) {
            return tasks;
        } else {
            throw new NotFoundException("Task not found with creator id: " + creatorId, tasks);
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

    public List<Task> findByTeam(Long teamId) throws NotFoundException {
        List<Task> tasks = taskRepository.findByTeam(teamId);
        if (!tasks.isEmpty()) {
            return tasks;
        } else {
            throw new NotFoundException("Task not found with team id: " + teamId, tasks);
        }
    }

    public List<Task> findByStatus(String status) throws NotFoundException {
        List<Task> tasks = taskRepository.findByStatus(status);
        if (!tasks.isEmpty()) {
            return tasks;
        } else {
            throw new NotFoundException("Task not found with status: " + status, tasks);
        }
    }


    public List<Task> findByDateRange(String startDateStr, String endDateStr) throws NotFoundException {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
            LocalDate endDate = LocalDate.parse(endDateStr, formatter);

            List<Task> tasks = taskRepository.findByDateRange(startDate, endDate);

            if (tasks.isEmpty()) {
                throw new NotFoundException("No tasks found within the date range: " + startDateStr + " to " + endDateStr, Task.class);
            }

            return tasks;

        } catch (DateTimeParseException e) {
            throw new RuntimeException("Error parsing date: " + e.getMessage());
        }
    }



}
