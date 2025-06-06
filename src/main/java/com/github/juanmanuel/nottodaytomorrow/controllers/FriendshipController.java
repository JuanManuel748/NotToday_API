package com.github.juanmanuel.nottodaytomorrow.controllers;

import com.github.juanmanuel.nottodaytomorrow.exceptions.NotFoundException;
import com.github.juanmanuel.nottodaytomorrow.models.Friendship;
import com.github.juanmanuel.nottodaytomorrow.models.FriendshipId;
import com.github.juanmanuel.nottodaytomorrow.models.User;
import com.github.juanmanuel.nottodaytomorrow.services.FriendshipService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/friendships")
public class FriendshipController {
    @Autowired
    private FriendshipService friendshipService;

    @Operation(summary = "Crea una relación de amistad entre dos usuarios")
    @PostMapping("/{requesterId}/request/{targetId}")
    public ResponseEntity<?> sendFriendRequest(@PathVariable Long requesterId, @PathVariable Long targetId) {
        try {
            Friendship friendship = friendshipService.sendFriendRequest(requesterId, targetId);
            return ResponseEntity.status(HttpStatus.CREATED).body(friendship);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Acepta una solicitud de amistad")
    @PutMapping("/{acceptorId}/accept/{requesterId}")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable Long acceptorId, @PathVariable Long requesterId) {
        try {
            Friendship friendship = friendshipService.acceptFriendRequest(acceptorId, requesterId);
            return ResponseEntity.ok(friendship);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Rechaza una solicitud de amistad")
    @DeleteMapping("/{declinerId}/decline/{requesterId}")
    public ResponseEntity<?> declineFriendRequest(@PathVariable Long declinerId, @PathVariable Long requesterId) {
        try {
            friendshipService.declineFriendRequest(declinerId, requesterId);
            return ResponseEntity.ok("Solicitud de amistad rechazada y eliminada.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Cancela una solicitud de amistad")
    @DeleteMapping("/{removerId}/remove/{friendToRemoveId}")
    public ResponseEntity<?> removeFriend(@PathVariable Long removerId, @PathVariable Long friendToRemoveId) {
        try {
            friendshipService.removeFriend(removerId, friendToRemoveId);
            return ResponseEntity.ok("Amigo eliminado correctamente.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Elimina una relación de amistad")
    @PutMapping("/{blockerId}/block/{userToBlockId}")
    public ResponseEntity<?> blockUser(@PathVariable Long blockerId, @PathVariable Long userToBlockId) {
        try {
            Friendship friendship = friendshipService.blockUser(blockerId, userToBlockId);
            return ResponseEntity.ok(friendship);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Desbloquea un usuario previamente bloqueado")
    @PutMapping("/{unblockerId}/unblock/{userToUnblockId}")
    public ResponseEntity<?> unblockUser(@PathVariable Long unblockerId, @PathVariable Long userToUnblockId) {
        try {
            friendshipService.unblockUser(unblockerId, userToUnblockId);
            return ResponseEntity.ok("Usuario desbloqueado correctamente.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Busca las relaciones de amistad de un usuario por su id")
    @GetMapping("/{userId}")
    public ResponseEntity<List<User>> getFriends(@PathVariable Long userId) {
        try {
            List<User> friends = friendshipService.getFriends(userId);
            return ResponseEntity.ok(friends);
        } catch (NotFoundException e) {
            throw new NotFoundException("Could not find friends for user with ID: " + userId, e);
        }
    }

    @Operation(summary = "Busca las peticiones de amistad pendientes entrantes de un usuario por su id")
    @GetMapping("/{userId}/requests/incoming")
    public ResponseEntity<List<Friendship>> getIncomingPendingRequests(@PathVariable Long userId) {
        try {
            List<Friendship> requests = friendshipService.getIncomingPendingRequests(userId);
            return ResponseEntity.ok(requests);
        } catch (NotFoundException e) {
            throw new NotFoundException("Could not find friends for user with ID: " + userId, e);
        }
    }

    @Operation(summary = "Busca las peticiones de amistad pendientes salientes de un usuario por su id")
    @GetMapping("/{userId}/requests/outgoing")
    public ResponseEntity<List<Friendship>> getOutgoingPendingRequests(@PathVariable Long userId) {
        try {
            List<Friendship> requests = friendshipService.getOutgoingPendingRequests(userId);
            return ResponseEntity.ok(requests);
        } catch (NotFoundException e) {
            throw new NotFoundException("Could not find friends for user with ID: " + userId, e);
        }
    }

    @Operation(summary = "Busca el estado de amistad entre dos usuarios")
    @GetMapping("/{userId1}/{userId2}")
    public ResponseEntity<Friendship> getFriendshipStatus(@PathVariable Long userId1, @PathVariable Long userId2) {
        try {
            Optional<Friendship> result = friendshipService.getFriendshipStatus(userId1, userId2);
            return result
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (NotFoundException e) {
            throw new NotFoundException("Could not find friends for user with ID: " + userId1, e);
        }
    }

    @Operation(summary = "Busca los usuarios bloqueados por un usuario")
    @GetMapping("/{userId}/blocked")
    public ResponseEntity<?> getBlockedUsers(@PathVariable Long userId) {
        try {
            List<User> blockedUsers = friendshipService.getBlockedUsers(userId);
            return ResponseEntity.ok(blockedUsers);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario con ID " + userId + " no encontrado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud.");
        }
    }
}