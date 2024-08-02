package com.example.eventurejavabackend.controller;

import com.example.eventurejavabackend.dto.ParticipantDTO;
import com.example.eventurejavabackend.model.Event;
import com.example.eventurejavabackend.model.Participant;
import com.example.eventurejavabackend.model.User;
import com.example.eventurejavabackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<User> addParticipant(@RequestHeader String userId, @RequestHeader String eventId, @RequestBody ParticipantDTO participantDTO) {
        Participant participant = new Participant();
        participant.setParticipantId(UUID.randomUUID().toString());
        participant.setUserId(participantDTO.getUserId());
        participant.setRole(participantDTO.getRole());

        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            for (Event event : user.getEvents()) {
                if (event.getEventId().equals(eventId)) {
                    event.getParticipants().add(participant);
                }
            }
            userService.updateUser(userId, user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateParticipant(@RequestHeader String userId, @RequestHeader String eventId, @RequestHeader String participantId, @RequestBody ParticipantDTO participantDTO) {
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            for (Event event : user.getEvents()) {
                if (event.getEventId().equals(eventId)) {
                    for (Participant p : event.getParticipants()) {
                        if (p.getParticipantId().equals(participantId)) {
                            p.setUserId(participantDTO.getUserId());
                            p.setRole(participantDTO.getRole());
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
    public ResponseEntity<User> deleteParticipant(@RequestHeader String userId, @RequestHeader String eventId, @RequestHeader String participantId) {
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            for (Event event : user.getEvents()) {
                if (event.getEventId().equals(eventId)) {
                    event.getParticipants().removeIf(participant -> participant.getParticipantId().equals(participantId));
                }
            }
            userService.updateUser(userId, user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}