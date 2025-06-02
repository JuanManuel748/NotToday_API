package com.github.juanmanuel.nottodaytomorrow.controllers;

import com.github.juanmanuel.nottodaytomorrow.models.Friendship;
import com.github.juanmanuel.nottodaytomorrow.models.User;
import com.github.juanmanuel.nottodaytomorrow.services.FriendshipService;
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

    @PostMapping("/request")
    public ResponseEntity<?> sendFriendRequest(@RequestParam Long requesterId, @RequestParam Long targetId) {
        try {
            Friendship friendship = friendshipService.sendFriendRequest(requesterId, targetId);
            return ResponseEntity.status(HttpStatus.CREATED).body(friendship);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/accept")
    public ResponseEntity<?> acceptFriendRequest(@RequestParam Long acceptorId, @RequestParam Long requesterId) {
        try {
            Friendship friendship = friendshipService.acceptFriendRequest(acceptorId, requesterId);
            return ResponseEntity.ok(friendship);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/decline")
    public ResponseEntity<?> declineFriendRequest(@RequestParam Long declinerId, @RequestParam Long requesterId) {
        try {
            friendshipService.declineFriendRequest(declinerId, requesterId);
            return ResponseEntity.ok("Solicitud de amistad rechazada y eliminada.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFriend(@RequestParam Long removerId, @RequestParam Long friendToRemoveId) {
        try {
            friendshipService.removeFriend(removerId, friendToRemoveId);
            return ResponseEntity.ok("Amigo eliminado correctamente.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/block")
    public ResponseEntity<?> blockUser(@RequestParam Long blockerId, @RequestParam Long userToBlockId) {
        try {
            Friendship friendship = friendshipService.blockUser(blockerId, userToBlockId);
            return ResponseEntity.ok(friendship);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/unblock")
    public ResponseEntity<?> unblockUser(@RequestParam Long unblockerId, @RequestParam Long userToUnblockId) {
        try {
            friendshipService.unblockUser(unblockerId, userToUnblockId);
            return ResponseEntity.ok("Usuario desbloqueado correctamente.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<?> getFriends(@PathVariable Long userId) {
        try {
            List<User> friends = friendshipService.getFriends(userId);
            return ResponseEntity.ok(friends);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/requests/incoming")
    public ResponseEntity<?> getIncomingPendingRequests(@PathVariable Long userId) {
        try {
            List<Friendship> requests = friendshipService.getIncomingPendingRequests(userId);
            return ResponseEntity.ok(requests);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/requests/outgoing")
    public ResponseEntity<?> getOutgoingPendingRequests(@PathVariable Long userId) {
        try {
            List<Friendship> requests = friendshipService.getOutgoingPendingRequests(userId);
            return ResponseEntity.ok(requests);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<?> getFriendshipStatus(@RequestParam Long userId1, @RequestParam Long userId2) {
        try {
            Optional<Friendship> friendshipStatus = friendshipService.getFriendshipStatus(userId1, userId2);
            return friendshipStatus.<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe relación de amistad o bloqueo entre los usuarios."));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}