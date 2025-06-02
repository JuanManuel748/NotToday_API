package com.github.juanmanuel.nottodaytomorrow.repositories;

import com.github.juanmanuel.nottodaytomorrow.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Encuentra todos los mensajes entre dos usuarios, ordenados por fecha de envío.
     */
    @Query(
            "SELECT m FROM Message m WHERE (m.sender.id = :userId1 AND m.receiver.id = :userId2) OR (m.sender.id = :userId2 AND m.receiver.id = :userId1) ORDER BY m.sentAt ASC")
    List<Message> findConversation(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    /**
     * Encuentra todos los mensajes recibidos por un usuario que no han sido leídos.
     */
    @Query("SELECT m FROM Message m WHERE m.receiver.id = :userId AND m.readAt IS NULL ORDER BY m.sentAt ASC")
    List<Message> findUnreadMessagesForUser(@Param("userId") Long userId);

    /**
     * Encuentra todos los mensajes enviados por un usuario, ordenados por fecha de envío descendente.
     */
    List<Message> findBySenderIdOrderBySentAtDesc(Long senderId);

    /**
     * Encuentra todos los mensajes recibidos por un usuario, ordenados por fecha de envío descendente.
     */
    List<Message> findByReceiverIdOrderBySentAtDesc(Long receiverId);
}