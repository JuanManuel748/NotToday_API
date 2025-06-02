package com.github.juanmanuel.nottodaytomorrow.repositories;

import com.github.juanmanuel.nottodaytomorrow.models.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    @Query(
            value = "SELECT * FROM bills AS b WHERE b.amount > ?1 AND b.amount < ?2",
            nativeQuery = true
    )
    List<Bill> findByAmountRange(BigDecimal min, BigDecimal max);
    @Query(
            value = "SELECT * FROM bills AS b WHERE b.description LIKE %?1%",
            nativeQuery = true
    )
    List<Bill> findByDescription(String description);
    @Query(
            value = "SELECT * FROM bills AS b WHERE b.team_id = ?1",
            nativeQuery = true
    )
    List<Bill> findByTeam(Long teamId);
    @Query(
            value = "SELECT * FROM bills AS b WHERE b.payer_id = ?1",
            nativeQuery = true
    )
    List<Bill> findByPayer(Long payerId);
    @Query(
            value = "SELECT * FROM bills AS b WHERE b.team_id = ?1 AND b.payer_id = ?2",
            nativeQuery = true
    )
    List<Bill> findByTeamAndPayer(Long teamId, Long payerId);
}
