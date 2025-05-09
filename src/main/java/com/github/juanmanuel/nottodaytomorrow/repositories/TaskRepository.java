package com.github.juanmanuel.nottodaytomorrow.repositories;

import com.github.juanmanuel.nottodaytomorrow.models.Task;
import com.github.juanmanuel.nottodaytomorrow.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(
            value = "SELECT * FROM tasks AS t WHERE t.name LIKE %?1%",
            nativeQuery = true
    )
    List<Task> findByName(String name);
    @Query(
            value = "SELECT * FROM tasks AS t WHERE t.creator_id = ?1",
            nativeQuery = true
    )
    List<Task> findByCreator(Long creatorId);
    @Query(
            value = "SELECT * FROM tasks AS t WHERE t.assigned_id = ?1",
            nativeQuery = true
    )
    List<Task> findByAssigned(Long creatorId);
    @Query(
            value = "SELECT * FROM tasks AS t WHERE t.team_id = ?1",
            nativeQuery = true
    )
    List<Task> findByTeam(Long teamId);
    @Query(
            value = "SELECT * FROM tasks AS t WHERE t.status = ?1",
            nativeQuery = true
    )
    List<Task> findByStatus(String creatorId);
}
