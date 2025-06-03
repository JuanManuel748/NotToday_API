package com.github.juanmanuel.nottodaytomorrow.repositories;

import com.github.juanmanuel.nottodaytomorrow.models.Task;
import com.github.juanmanuel.nottodaytomorrow.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(
            value = "SELECT * FROM tasks AS t WHERE t.assigned_id = ?1 ORDER BY t.limit_date ASC",
            nativeQuery = true
    )
    List<Task> findByAssigned(Long creatorId);
    @Query(
            value = "SELECT * FROM tasks AS t WHERE t.assigned_id = ?1 && t.state != 'COMPLETADA' ORDER BY t.limit_date ASC",
            nativeQuery = true
    )
    List<Task> findByAssignedNotComp(Long creatorId);
    @Query(
            value = "SELECT * FROM tasks AS t WHERE t.team_id = ?1 ORDER BY t.limit_date ASC",
            nativeQuery = true
    )
    List<Task> findByTeam(Long teamId);
    @Query(
            value = "SELECT * FROM tasks AS t WHERE t.team_id = ?1 AND t.state != 'COMPLETADA' ORDER BY t.limit_date ASC",
            nativeQuery = true
    )
    List<Task> findByTeamNotCompleted(Long teamId);
    @Query(
            value = "SELECT * FROM tasks AS t WHERE t.state = ?2 && t.team_id = ?1",
            nativeQuery = true
    )
    List<Task> findByStatus(Long teamId, String creatorId);

    @Query(
            "SELECT t FROM Task t WHERE t.state = 'PENDIENTE' AND t.limitDate < :currentDateTime"
    )
    List<Task> findTasksByStateAndLimitDateBefore(LocalDateTime currentDateTime);
    @Query(
            "SELECT COUNT(t.id) FROM Task t WHERE t.state = 'COMPLETADA' AND t.team.id = ?1 AND t.assigned.id = ?2"
    )
    Long countCompletedTasksByAssignedNteam(Long teamId, Long userId);

}
