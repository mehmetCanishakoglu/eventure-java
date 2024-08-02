package com.example.eventurejavabackend.controller;

import com.example.eventurejavabackend.model.Event;
import com.example.eventurejavabackend.model.User;
import com.example.eventurejavabackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventController {
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<User> addEvent(@RequestHeader String userId, @RequestBody Event event) {
        event.setEventId(UUID.randomUUID().toString());
        if (event.getTasks() == null) {
            event.setTasks(new ArrayList<>());
        }
        if (event.getParticipants() == null) {
            event.setParticipants(new ArrayList<>());
        }
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getEvents().add(event);
            userService.updateUser(userId, user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateEvent(@RequestHeader String userId, @RequestHeader String eventId, @RequestBody Event event) {
        if (event.getTasks() == null) {
            event.setTasks(new ArrayList<>());
        }
        if (event.getParticipants() == null) {
            event.setParticipants(new ArrayList<>());
        }
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            for (Event e : user.getEvents()) {
                if (e.getEventId().equals(eventId)) {
                    e.setTitle(event.getTitle());
                    e.setDescription(event.getDescription());
                    e.setDate(event.getDate());
                    e.setLocation(event.getLocation());
                    e.setTasks(event.getTasks());
                    e.setParticipants(event.getParticipants());
                }
            }
            userService.updateUser(userId, user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<User> deleteEvent(@RequestHeader String userId, @RequestHeader String eventId) {
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getEvents().removeIf(event -> event.getEventId().equals(eventId));
            userService.updateUser(userId, user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}