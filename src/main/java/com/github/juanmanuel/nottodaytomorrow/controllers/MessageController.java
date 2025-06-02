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
    public ResponseEntity<?> sendMessage(@RequestBody Message msg) {
        try {
            // Validación básica para evitar NPE si la deserialización falla parcialmente
            // La causa raíz de la deserialización (msg.getSender() siendo null) debe ser arreglada en los modelos Message/User.
            if (msg == null) {
                return ResponseEntity.badRequest().body("Solicitud inválida: el objeto mensaje es nulo.");
            }
            if (msg.getSender() == null) {
                return ResponseEntity.badRequest().body("Solicitud inválida: el remitente es nulo.");
            }
            if (msg.getSender().getId() == null) {
                return ResponseEntity.badRequest().body("Solicitud inválida: el ID del remitente es nulo.");
            }
            if (msg.getReceiver() == null) {
                return ResponseEntity.badRequest().body("Solicitud inválida: el destinatario es nulo.");
            }
            if (msg.getReceiver().getId() == null) {
                return ResponseEntity.badRequest().body("Solicitud inválida: el ID del destinatario es nulo.");
            }
            if (msg.getContent() == null || msg.getContent().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Solicitud inválida: el contenido del mensaje está vacío o es nulo.");
            }

            Message message = messageService.sendMessage(msg.getSender().getId(), msg.getReceiver().getId(), msg.getContent());
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Loggear la excepción 'e' es una buena práctica aquí.
            // Considera si quieres exponer e.getMessage() al cliente.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error inesperado al enviar el mensaje.");
        }
    }

    @GetMapping("/{userId1}/{userId2}")
    public ResponseEntity<List<Message>> getConversation(@PathVariable Long userId1, @PathVariable Long userId2) {
        try {
            List<Message> conversation = messageService.getConversation(userId1, userId2);
            return ResponseEntity.ok(conversation);
        } catch (NotFoundException e) {
            throw new NotFoundException("Conversation between users with ids: " + userId1 + " and " + userId2 + " not found", Message.class);
        }
    }

    @PostMapping("/{messageId}/{readerId}/read")
    public ResponseEntity<Message> markMessageAsRead(@PathVariable Long messageId, @PathVariable Long readerId) {
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