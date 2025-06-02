package com.github.juanmanuel.nottodaytomorrow.controllers;

import com.github.juanmanuel.nottodaytomorrow.exceptions.NotFoundException;
import com.github.juanmanuel.nottodaytomorrow.models.Message;
import com.github.juanmanuel.nottodaytomorrow.services.MessageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;


    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody Message msg) {
        try {
            Message message = messageService.sendMessage(msg.getSender().getId(), msg.getReceiver().getId(), msg.getContent());
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (NotFoundException e) {
            throw new NotFoundException("Sender or receiver not found", Message.class);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/conversation")
    public ResponseEntity<List<Message>> getConversation(@RequestParam Long userId1, @RequestParam Long userId2) {
        try {
            List<Message> conversation = messageService.getConversation(userId1, userId2);
            return ResponseEntity.ok(conversation);
        } catch (NotFoundException e) {
            throw new NotFoundException("Conversation between users with ids: " + userId1 + " and " + userId2 + " not found", Message.class);
        }
    }

    @PostMapping("/{messageId}/read")
    public ResponseEntity<Message> markMessageAsRead(@PathVariable Long messageId, @RequestParam Long readerId) {
        try {
            Optional<Message> message = messageService.markMessageAsRead(messageId, readerId);
            return message.isPresent() ? ResponseEntity.ok(message.get()) : ResponseEntity.notFound().build();
        } catch (NotFoundException e) {
            throw new NotFoundException("Message with id: " + messageId + " not found", Message.class);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }

    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<Message>> getUnreadMessagesCount(@PathVariable Long userId) {
        try {
            List<Message> messages = messageService.getUnreadMessages(userId);
            return ResponseEntity.ok(messages);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Couln't find messages unread from user with id: " + userId, Message.class);
        }
    }

}