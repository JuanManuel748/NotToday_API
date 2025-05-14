package com.github.juanmanuel.nottodaytomorrow.repositories;

import com.github.juanmanuel.nottodaytomorrow.models.BillsUser;
import com.github.juanmanuel.nottodaytomorrow.models.BillsUserId;
import com.github.juanmanuel.nottodaytomorrow.models.Comment;
import com.github.juanmanuel.nottodaytomorrow.models.CommentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillsUserRepository extends JpaRepository<BillsUser, BillsUserId> {
    @Query(
            value = "SELECT * FROM bills_users AS bu WHERE bu.user_id = ?1 AND bu.bill_id = ?2",
            nativeQuery = true
    )
    Optional<BillsUser> findByPK(Long userId, Long billId);

    @Query(
            value = "SELECT * FROM bills_users AS bu WHERE bu.user_id = ?1",
            nativeQuery = true
    )
    List<Comment> findByUser(Long userId);
    @Query(
            value = "SELECT * FROM bills_users AS bu WHERE bu.user_id = ?1 AND bu.owed > bu.paid",
            nativeQuery = true
    )
    List<Comment> findByUserNotPaid(Long userId);
    @Query(
            value = "SELECT * FROM bills_users AS bu WHERE bu.user_id = ?1 AND bu.owed = bu.paid",
            nativeQuery = true
    )
    List<Comment> findByUserPaid(Long userId);
}
