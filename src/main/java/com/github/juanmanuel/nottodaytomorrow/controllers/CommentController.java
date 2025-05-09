package com.github.juanmanuel.nottodaytomorrow.controllers;

import com.github.juanmanuel.nottodaytomorrow.models.Comment;
import com.github.juanmanuel.nottodaytomorrow.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Operation(summary = "Crea un comentario")
    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        Comment createdComment = commentService.create(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @Operation(summary = "Actualiza un comentario")
    @PutMapping("/{userId}/{commenterId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long userId, @PathVariable Long commenterId, @RequestBody Comment updatedComment) {
        Comment commentUpdated = commentService.update(userId, commenterId, updatedComment);
        return ResponseEntity.status(HttpStatus.OK).body(commentUpdated);
    }

    @Operation(summary = "Eliminar un comentario por su id")
    @DeleteMapping("/{userId}/{commenterId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long userId, @PathVariable Long commenterId) {
        if (commentService.delete(userId, commenterId)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Buscar un comentario por su id")
    @GetMapping("/{userId}/{commenterId}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long userId, @PathVariable Long commenterId) {
        Comment comment = commentService.getById(userId, commenterId);
        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }

    @Operation(summary = "Buscar todos los comentarios")
    @GetMapping
    public ResponseEntity<List<Comment>> getAll() {
        List<Comment> comments = commentService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    @Operation(summary = "Buscar comentarios de un usuario")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Comment>> getUserComments(@PathVariable Long userId) {
        List<Comment> comments = commentService.findByReceiverId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    @Operation(summary = "Buscar comentarios hechos por el usuario")
    @GetMapping("/commenter/{commenterId}")
    public ResponseEntity<List<Comment>> getCommenterComments(@PathVariable Long commenterId) {
        List<Comment> comments = commentService.findByCommenterId(commenterId);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    @Operation(summary = "Buscar comentarios del usuario dentro de un rango de fechas")
    @GetMapping("/user/{userId}/date/{beforeDate}/{afterDate}")
    public ResponseEntity<List<Comment>> getUserCommentsByDate(@PathVariable Long userId, @PathVariable String beforeDate, @PathVariable String afterDate) {
        LocalDate before = LocalDate.parse(beforeDate);
        LocalDate after = LocalDate.parse(afterDate);

        List<Comment> comments = commentService.findByUserDateRange(userId, before, after);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

}
