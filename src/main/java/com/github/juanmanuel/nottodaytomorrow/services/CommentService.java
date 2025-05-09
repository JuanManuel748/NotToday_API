package com.github.juanmanuel.nottodaytomorrow.services;

import com.github.juanmanuel.nottodaytomorrow.exceptions.NotFoundException;
import com.github.juanmanuel.nottodaytomorrow.models.Comment;
import com.github.juanmanuel.nottodaytomorrow.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getAll() {
        List<Comment> comments = commentRepository.findAll();
        if (comments.isEmpty()) {
            throw new RuntimeException("No comments found");
        }
        return comments;
    }

    public Comment getById(Long userId, Long commenterId) throws NotFoundException {
        Optional<Comment> comment = commentRepository.findByPK(userId, commenterId);
        if (comment.isPresent()) {
            return comment.get();
        } else {
            throw new RuntimeException("Comment not found with userId: " + userId + " and commenterId: " + commenterId);
        }
    }

    public Comment create(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment update(Long userId, Long commenterId, Comment updatedComment) throws NotFoundException {
        Optional<Comment> existing = commentRepository.findByPK(userId, commenterId);
        if (existing.isPresent()) {
            Comment updated = existing.get();
            updated.setComment(updatedComment.getComment());
            updated.setScore(updated.getScore());
            updated.setCommentDate(updatedComment.getCommentDate());
            updated.setAnonymous(updatedComment.getAnonymous());
            return commentRepository.save(updated);
        } else {
            throw new NotFoundException("Comment not found with userId: " + userId + " and commenterId: " + commenterId, Comment.class);
        }
    }

    public boolean delete(Long userID, Long commenterId) throws NotFoundException {
        Optional<Comment> comment = commentRepository.findByPK(userID, commenterId);
        if (comment.isPresent()) {
            commentRepository.delete(comment.get());
            return true;
        } else {
            throw new NotFoundException("Comment not found with id: " + userID + " - " + commenterId, Comment.class);
        }
    }

    public List<Comment> findByReceiverId(Long userId) {
        List<Comment> comments = commentRepository.findByReceiverId(userId);
        if(!comments.isEmpty()) {
            return comments;
        } else {
            throw new RuntimeException("No comments found for userId: " + userId);
        }
    }

    public List<Comment> findByCommenterId(Long comId) {
        List<Comment> comments = commentRepository.findByCommenterId(comId);
        if(!comments.isEmpty()) {
            return comments;
        } else {
            throw new RuntimeException("No comments found for commenterId: " + comId);
        }
    }

    public List<Comment> findByUserDateRange(Long userId, LocalDate beforeDate, LocalDate afterDate) {
        List<Comment> comments = commentRepository.findByUserDateRange(userId, beforeDate, afterDate);
        if(!comments.isEmpty()) {
            return comments;
        } else {
            throw new RuntimeException("No comments found for userId: " + userId + " between " + beforeDate + " and " + afterDate);
        }
    }
}
