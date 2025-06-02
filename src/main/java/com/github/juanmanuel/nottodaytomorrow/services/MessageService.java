package com.github.juanmanuel.nottodaytomorrow.services;

import com.github.juanmanuel.nottodaytomorrow.models.Friendship;
import com.github.juanmanuel.nottodaytomorrow.models.FriendshipId;
import com.github.juanmanuel.nottodaytomorrow.models.Message;
import com.github.juanmanuel.nottodaytomorrow.models.User;
import com.github.juanmanuel.nottodaytomorrow.repositories.MessageRepository;
import com.github.juanmanuel.nottodaytomorrow.repositories.UserRepository; // Asegúrate de tener este repositorio
import com.github.juanmanuel.nottodaytomorrow.repositories.FriendshipRepository; // Para verificar amistad si es necesario
import com.github.juanmanuel.nottodaytomorrow.services.FriendshipService; // Para constantes de estado
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository; // Opcional, para verificar amistad

    @Autowired
    public MessageService(MessageRepository messageRepository, UserRepository userRepository, FriendshipRepository friendshipRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userId));
    }

    @Transactional
    public Message sendMessage(Long senderId, Long receiverId, String content) {
        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("No puedes enviarte mensajes a ti mismo.");
        }
        User sender = findUserById(senderId);
        User receiver = findUserById(receiverId);

        // Opcional: Verificar si son amigos antes de permitir el mensaje
         FriendshipId friendshipId = new FriendshipId(senderId, receiverId);
         Optional<Friendship> friendshipOpt = friendshipRepository.findById(friendshipId);
         if (friendshipOpt.isEmpty() || !FriendshipService.STATUS_ACCEPTED.equals(friendshipOpt.get().getStatus())) {
             throw new IllegalStateException("Solo puedes enviar mensajes a tus amigos.");
         }
        // Opcional: Verificar si alguno ha bloqueado al otro
         if (friendshipOpt.isPresent()) {
            Friendship f = friendshipOpt.get();
            if ((f.getStatus().equals(FriendshipService.STATUS_BLOCKED_BY_USER1) && f.getUser1().getId().equals(senderId)) ||
                (f.getStatus().equals(FriendshipService.STATUS_BLOCKED_BY_USER2) && f.getUser2().getId().equals(senderId))) {
                throw new IllegalStateException("No puedes enviar un mensaje porque has bloqueado a este usuario.");
            }
            if ((f.getStatus().equals(FriendshipService.STATUS_BLOCKED_BY_USER1) && f.getUser1().getId().equals(receiverId)) ||
                (f.getStatus().equals(FriendshipService.STATUS_BLOCKED_BY_USER2) && f.getUser2().getId().equals(receiverId))) {
                throw new IllegalStateException("No puedes enviar un mensaje a un usuario que te ha bloqueado.");
            }
         }


        Message message = new Message(sender, receiver, content);
        return messageRepository.save(message);
    }

    public List<Message> getConversation(Long userId1, Long userId2) {
        findUserById(userId1); // Validar existencia
        findUserById(userId2);
        return messageRepository.findConversation(userId1, userId2);
    }

    @Transactional
    public Optional<Message> markMessageAsRead(Long messageId, Long readerId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException("Mensaje no encontrado con ID: " + messageId));

        if (!message.getReceiver().getId().equals(readerId)) {
            throw new IllegalArgumentException("Solo el destinatario puede marcar el mensaje como leído.");
        }

        if (message.getReadAt() == null) {
            message.setReadAt(LocalDateTime.now());
            return Optional.of(messageRepository.save(message));
        }
        return Optional.of(message); // Ya estaba leído
    }

    public List<Message> getUnreadMessages(Long userId) {
        findUserById(userId); // Validar existencia
        return messageRepository.findUnreadMessagesForUser(userId);
    }

    public List<Message> getSentMessages(Long userId) {
        findUserById(userId);
        return messageRepository.findBySenderIdOrderBySentAtDesc(userId);
    }

    public List<Message> getReceivedMessages(Long userId) {
        findUserById(userId);
        return messageRepository.findByReceiverIdOrderBySentAtDesc(userId);
    }
}