package com.example.eventurejavabackend.controller;

import com.example.eventurejavabackend.dto.TaskDTO;
import com.example.eventurejavabackend.model.Event;
import com.example.eventurejavabackend.model.Task;
import com.example.eventurejavabackend.model.User;
import com.example.eventurejavabackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<User> addTask(@RequestHeader String userId, @RequestHeader String eventId, @RequestBody TaskDTO taskDTO) {
        Task task = new Task();
        task.setTaskId(UUID.randomUUID().toString());
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setDeadline(taskDTO.getDeadline());

        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            for (Event event : user.getEvents()) {
                if (event.getEventId().equals(eventId)) {
                    event.getTasks().add(task);
                }
            }
            userService.updateUser(userId, user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateTask(@RequestHeader String userId, @RequestHeader String eventId, @RequestHeader String taskId, @RequestBody TaskDTO taskDTO) {
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            for (Event event : user.getEvents()) {
                if (event.getEventId().equals(eventId)) {
                    for (Task t : event.getTasks()) {
                        if (t.getTaskId().equals(taskId)) {
                            t.setTitle(taskDTO.getTitle());
                            t.setDescription(taskDTO.getDescription());
                            t.setStatus(taskDTO.getStatus());
                            t.setDeadline(taskDTO.getDeadline());
                        }
                    }
                }
            }
            userService.updateUser(userId, user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<User> deleteTask(@RequestHeader String userId, @RequestHeader String eventId, @RequestHeader String taskId) {
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            for (Event event : user.getEvents()) {
                if (event.getEventId().equals(eventId)) {
                    event.getTasks().removeIf(task -> task.getTaskId().equals(taskId));
                }
            }
            userService.updateUser(userId, user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}