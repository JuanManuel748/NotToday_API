package com.github.juanmanuel.nottodaytomorrow.repositories;

import com.github.juanmanuel.nottodaytomorrow.models.Friendship;
import com.github.juanmanuel.nottodaytomorrow.models.FriendshipId;
import com.github.juanmanuel.nottodaytomorrow.models.User;
import com.github.juanmanuel.nottodaytomorrow.services.FriendshipService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId> {

    /**
     * Encuentra todas las amistades aceptadas para un usuario.
     * user1 o user2 pueden ser el usuario dado.
     */
    @Query("SELECT f FROM Friendship f WHERE (f.user1.id = :userId OR f.user2.id = :userId) AND f.status = 'ACCEPTED'")
    List<Friendship> findAcceptedFriends(@Param("userId") Long userId);

    /**
     * Encuentra las solicitudes de amistad pendientes entrantes para un usuario.
     * :userId es el destinatario de la solicitud.
     * Si :userId es user1 (ID más pequeño), el estado debe ser PENDING_FROM_USER2_TO_USER1.
     * Si :userId es user2 (ID más grande), el estado debe ser PENDING_FROM_USER1_TO_USER2.
     */
    @Query("SELECT f FROM Friendship f WHERE (f.id.userId1 = :userId AND f.status = 'PENDING_U2_TO_U1') OR (f.id.userId2 = :userId AND f.status = 'PENDING_U1_TO_U2')")
    List<Friendship> findIncomingPendingRequests(@Param("userId") Long userId);

    /**
     * Encuentra las solicitudes de amistad pendientes enviadas por un usuario.
     * :userId es el remitente de la solicitud.
     * Si :userId es user1 (ID más pequeño), el estado debe ser PENDING_FROM_USER1_TO_USER2.
     * Si :userId es user2 (ID más grande), el estado debe ser PENDING_FROM_USER2_TO_USER1.
     */
    @Query("SELECT f FROM Friendship f WHERE (f.id.userId1 = :userId AND f.status = 'PENDING_U1_TO_U2') OR (f.id.userId2 = :userId AND f.status = 'PENDING_U2_TO_U1')")
    List<Friendship> findOutgoingPendingRequests(@Param("userId") Long userId);

    /**
     * Encuentra amistades donde un usuario está involucrado y tiene un estado específico.
     * Útil para estados como BLOQUEADO.
     */
    @Query("SELECT f FROM Friendship f WHERE (f.user1.id = :userId OR f.user2.id = :userId) AND f.status = :status")
    List<Friendship> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

    /**
     * Encuentra amistades donde user1 es el usuario dado y tiene un estado específico.
     * Útil para estados como BLOCKED_BY_USER1.
     */
    @Query("SELECT f FROM Friendship f WHERE f.user1.id = :userId AND f.status = :status")
    List<Friendship> findByUser1IdAndStatus(@Param("userId") Long userId, @Param("status") String status);

    /**
     * Encuentra amistades donde user2 es el usuario dado y tiene un estado específico.
     * Útil para estados como BLOCKED_BY_USER2.
     */
    @Query("SELECT f FROM Friendship f WHERE f.user2.id = :userId AND f.status = :status")
    List<Friendship> findByUser2IdAndStatus(@Param("userId") Long userId, @Param("status") String status);


    /**
     * Encuentra usuarios (user2) que han sido bloqueados por blockerId (user1).
     */
    @Query("SELECT f.user2 FROM Friendship f WHERE f.id.userId1 = :blockerId AND f.status = '" + FriendshipService.STATUS_BLOCKED_BY_USER1 + "'")
    List<User> findBlockedUsersWhereBlockerIsUser1(@Param("blockerId") Long blockerId);

    /**
     * Encuentra usuarios (user1) que han sido bloqueados por blockerId (user2).
     */
    @Query("SELECT f.user1 FROM Friendship f WHERE f.id.userId2 = :blockerId AND f.status = '" + FriendshipService.STATUS_BLOCKED_BY_USER2 + "'")
    List<User> findBlockedUsersWhereBlockerIsUser2(@Param("blockerId") Long blockerId);
}