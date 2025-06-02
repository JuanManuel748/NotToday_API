package com.github.juanmanuel.nottodaytomorrow.services;

import com.github.juanmanuel.nottodaytomorrow.models.Friendship;
import com.github.juanmanuel.nottodaytomorrow.models.FriendshipId;
import com.github.juanmanuel.nottodaytomorrow.models.User;
import com.github.juanmanuel.nottodaytomorrow.repositories.FriendshipRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendshipService {
    @Autowired
    private FriendshipRepository friendshipRepository;
    @Autowired
    private UserService userService;

    // Constantes para los estados de amistad
    // U1 se refiere al usuario con el ID más pequeño en la relación, U2 al ID más grande.
    public static final String STATUS_PENDING_FROM_USER1_TO_USER2 = "PENDING_U1_TO_U2";
    public static final String STATUS_PENDING_FROM_USER2_TO_USER1 = "PENDING_U2_TO_U1";
    public static final String STATUS_ACCEPTED = "ACCEPTED";
    public static final String STATUS_DECLINED = "DECLINED"; // Podría eliminarse después de un tiempo
    public static final String STATUS_BLOCKED_BY_USER1 = "BLOCKED_BY_U1"; // U1 bloqueó a U2
    public static final String STATUS_BLOCKED_BY_USER2 = "BLOCKED_BY_U2"; // U2 bloqueó a U1



    @Transactional
    public Friendship sendFriendRequest(Long requesterId, Long targetId) {
        if (requesterId.equals(targetId)) {
            throw new IllegalArgumentException("No puedes enviarte una solicitud de amistad a ti mismo.");
        }

        User requester = userService.getById(requesterId);
        User target = userService.getById(targetId);

        FriendshipId friendshipId = new FriendshipId(requesterId, targetId);
        Optional<Friendship> existingFriendshipOpt = friendshipRepository.findById(friendshipId);

        if (existingFriendshipOpt.isPresent()) {
            Friendship existingFriendship = existingFriendshipOpt.get();
            String status = existingFriendship.getStatus();
            if (STATUS_ACCEPTED.equals(status)) {
                throw new IllegalStateException("Ya son amigos.");
            }
            if (status.startsWith("PENDING_")) {
                throw new IllegalStateException("Ya existe una solicitud de amistad pendiente.");
            }
            if (status.startsWith("BLOCKED_")) {
                throw new IllegalStateException("No se puede enviar una solicitud de amistad a un usuario bloqueado o que te ha bloqueado.");
            }
            if (STATUS_DECLINED.equals(status)) {
                friendshipRepository.delete(existingFriendship); // Eliminar la solicitud anterior
            }
        }

        String newStatus;
        User user1, user2;

        if (friendshipId.getUserId1().equals(requesterId)) { // Requester es User1 (ID más pequeño)
            newStatus = STATUS_PENDING_FROM_USER1_TO_USER2;
            user1 = requester;
            user2 = target;
        } else { // Requester es User2 (ID más grande)
            newStatus = STATUS_PENDING_FROM_USER2_TO_USER1;
            user1 = target; // user1 en la entidad Friendship es el de ID menor
            user2 = requester;
        }

        Friendship friendship = new Friendship(user1, user2, newStatus);
        return friendshipRepository.save(friendship);
    }

    @Transactional
    public Friendship acceptFriendRequest(Long acceptorId, Long requesterId) {

        FriendshipId friendshipId = new FriendshipId(acceptorId, requesterId);
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud de amistad no encontrada."));

        // Determinar quién es user1 y user2 en la amistad almacenada
        Long storedUser1Id = friendship.getId().getUserId1();
        // Long storedUser2Id = friendship.getId().getUserId2();

        // Validar que la solicitud esté pendiente y dirigida al aceptador
        boolean isValidPendingRequest = (friendship.getStatus().equals(STATUS_PENDING_FROM_USER1_TO_USER2) && acceptorId.equals(storedUser1Id == requesterId ? friendshipId.getUserId2() : friendshipId.getUserId1())) ||
                (friendship.getStatus().equals(STATUS_PENDING_FROM_USER2_TO_USER1) && acceptorId.equals(storedUser1Id == requesterId ? friendshipId.getUserId1() : friendshipId.getUserId2()));

        // Simplificación de la validación: el aceptador debe ser el destinatario de la solicitud pendiente
        boolean isTargetOfU1ToU2 = friendship.getStatus().equals(STATUS_PENDING_FROM_USER1_TO_USER2) && acceptorId.equals(friendship.getUser2().getId());
        boolean isTargetOfU2ToU1 = friendship.getStatus().equals(STATUS_PENDING_FROM_USER2_TO_USER1) && acceptorId.equals(friendship.getUser1().getId());


        if (!(isTargetOfU1ToU2 || isTargetOfU2ToU1)) {
            throw new IllegalStateException("No hay una solicitud de amistad válida para aceptar o no eres el destinatario.");
        }

        friendship.setStatus(STATUS_ACCEPTED);
        friendship.setCreatedAt(LocalDateTime.now()); // Actualizar timestamp si se desea
        return friendshipRepository.save(friendship);
    }

    @Transactional
    public Friendship declineFriendRequest(Long declinerId, Long requesterId) {

        FriendshipId friendshipId = new FriendshipId(declinerId, requesterId);
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud de amistad no encontrada."));

        boolean isTargetOfU1ToU2 = friendship.getStatus().equals(STATUS_PENDING_FROM_USER1_TO_USER2) && declinerId.equals(friendship.getUser2().getId());
        boolean isTargetOfU2ToU1 = friendship.getStatus().equals(STATUS_PENDING_FROM_USER2_TO_USER1) && declinerId.equals(friendship.getUser1().getId());

        if (!(isTargetOfU1ToU2 || isTargetOfU2ToU1)) {
            throw new IllegalStateException("No hay una solicitud de amistad válida para rechazar o no eres el destinatario.");
        }

        // Opción 1: Cambiar estado a DECLINED
        // friendship.setStatus(STATUS_DECLINED);
        // return friendshipRepository.save(friendship);
        // Opción 2: Eliminar la solicitud (más común para "rechazar")
        friendshipRepository.delete(friendship);
        return friendship; // Devuelve la entidad eliminada (ya no está en BD) o void
    }

    @Transactional
    public void removeFriend(Long removerId, Long friendToRemoveId) {
        FriendshipId friendshipId = new FriendshipId(removerId, friendToRemoveId);
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new EntityNotFoundException("Relación de amistad no encontrada."));

        if (!friendship.getStatus().equals(STATUS_ACCEPTED)) {
            throw new IllegalStateException("No son amigos o la relación no está aceptada.");
        }
        friendshipRepository.delete(friendship);
    }

    @Transactional
    public Friendship blockUser(Long blockerId, Long userToBlockId) {
        if (blockerId.equals(userToBlockId)) {
            throw new IllegalArgumentException("No puedes bloquearte a ti mismo.");
        }
        User blocker = userService.getById(blockerId);
        User userToBlock = userService.getById(userToBlockId);

        FriendshipId friendshipId = new FriendshipId(blockerId, userToBlockId);
        String newStatus;

        if (friendshipId.getUserId1().equals(blockerId)) { // Blocker es User1 (ID más pequeño)
            newStatus = STATUS_BLOCKED_BY_USER1;
        } else { // Blocker es User2 (ID más grande)
            newStatus = STATUS_BLOCKED_BY_USER2;
        }

        Friendship friendship = friendshipRepository.findById(friendshipId).orElse(null);
        if (friendship == null) {
            // Asegurar que user1 y user2 se asignan correctamente según el ID en FriendshipId
            User u1Entity = friendshipId.getUserId1().equals(blocker.getId()) ? blocker : userToBlock;
            User u2Entity = friendshipId.getUserId2().equals(userToBlock.getId()) ? userToBlock : blocker;
            friendship = new Friendship(u1Entity, u2Entity, newStatus);
        } else {
            friendship.setStatus(newStatus);
            friendship.setCreatedAt(LocalDateTime.now()); // Actualizar timestamp
        }
        return friendshipRepository.save(friendship);
    }

    @Transactional
    public void unblockUser(Long unblockerId, Long userToUnblockId) {
        FriendshipId friendshipId = new FriendshipId(unblockerId, userToUnblockId);
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new EntityNotFoundException("Relación de bloqueo no encontrada."));

        boolean isBlockedByU1 = friendship.getStatus().equals(STATUS_BLOCKED_BY_USER1) && unblockerId.equals(friendship.getUser1().getId());
        boolean isBlockedByU2 = friendship.getStatus().equals(STATUS_BLOCKED_BY_USER2) && unblockerId.equals(friendship.getUser2().getId());

        if (!(isBlockedByU1 || isBlockedByU2)) {
            throw new IllegalStateException("El usuario no está bloqueado por ti o la relación de bloqueo es incorrecta.");
        }

        // Al desbloquear, se elimina la entrada de amistad/bloqueo.
        // Podría cambiarse a un estado neutro si se prefiere mantener el historial.
        friendshipRepository.delete(friendship);
    }

    public List<User> getFriends(Long userId) {
        List<Friendship> acceptedFriendships = friendshipRepository.findAcceptedFriends(userId);
        return acceptedFriendships.stream()
                .map(f -> f.getUser1().getId().equals(userId) ? f.getUser2() : f.getUser1())
                .collect(Collectors.toList());
    }

    public List<Friendship> getIncomingPendingRequests(Long userId) {
        return friendshipRepository.findIncomingPendingRequests(userId);
    }

    public List<Friendship> getOutgoingPendingRequests(Long userId) {
        return friendshipRepository.findOutgoingPendingRequests(userId);
    }

    public Optional<Friendship> getFriendshipStatus(Long userId1, Long userId2) {
        if (userId1.equals(userId2)) return Optional.empty();
        FriendshipId friendshipId = new FriendshipId(userId1, userId2);
        return friendshipRepository.findById(friendshipId);
    }

    public List<User> getBlockedUsers(Long blockerId) {
        // Validar que el usuario blockerId existe
        userService.getById(blockerId); // Esto lanzará EntityNotFoundException si no existe

        List<User> blockedByBlockerAsUser1 = friendshipRepository.findBlockedUsersWhereBlockerIsUser1(blockerId);
        List<User> blockedByBlockerAsUser2 = friendshipRepository.findBlockedUsersWhereBlockerIsUser2(blockerId);

        // Combinar las dos listas
        // Usar Stream para evitar duplicados si fuera posible (aunque aquí no debería haberlos)
        // y para una forma más funcional de combinar.
        List<User> allBlockedUsers = new ArrayList<>(blockedByBlockerAsUser1);
        allBlockedUsers.addAll(blockedByBlockerAsUser2);

        return allBlockedUsers;
    }
}