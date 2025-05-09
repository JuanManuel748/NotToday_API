package com.github.juanmanuel.nottodaytomorrow.repositories;

import com.github.juanmanuel.nottodaytomorrow.models.Comment;
import com.github.juanmanuel.nottodaytomorrow.models.CommentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, CommentId> {
    // Custom query methods can be defined here if needed
    @Query(
            value = "SELECT * FROM comments AS c WHERE c.user_id = ?1",
            nativeQuery = true
    )
    List<Comment> findByReceiverId(Long recipeId);
    @Query(
            value = "SELECT * FROM comments AS c WHERE c.commenter_id = ?1",
            nativeQuery = true
    )
    List<Comment> findByCommenterId(Long commenterId);
    @Query(
            value = "SELECT * FROM comments AS c WHERE c.user_id = ?1 && c.commenter_id = ?2",
            nativeQuery = true
    )
    Optional<Comment> findByPK(Long userId, Long commenterId);
    @Query(
            value = "SELECT * FROM comments AS c " +
                    "WHERE c.user_id = ?1 AND c.comment_date BETWEEN ?2 AND ?3",
            nativeQuery = true
    )
    List<Comment> findByUserDateRange(Long userId, LocalDate beforeDate, LocalDate afterDate);
}
