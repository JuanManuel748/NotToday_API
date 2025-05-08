package com.github.juanmanuel.nottodaytomorrow.repositories;

import com.github.juanmanuel.nottodaytomorrow.models.Team;
import com.github.juanmanuel.nottodaytomorrow.models.User;
import com.github.juanmanuel.nottodaytomorrow.models.UsersTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersTeamRepository extends JpaRepository<UsersTeam, Long> {
    @Query (
            value = "SELECT * FROM users_teams AS ut WHERE ut.usersid = ?1 AND ut.teamsid = ?2",
            nativeQuery = true
    )
    Optional<UsersTeam> findByUserAndTeam(Long userId, Long teamId);
    @Query(
            value = "SELECT * FROM users_teams AS ut WHERE ut.usersid = ?1",
            nativeQuery = true
    )
    List<UsersTeam> findByUser(Long userId);
    @Query(
            value = "SELECT * FROM users_teams AS ut WHERE ut.teamsid = ?1",
            nativeQuery = true
    )
    List<UsersTeam> findByTeam(Long teamId);
    @Query(
            value = "SELECT u.* FROM users u"
            + "JOIN users_teams ut ON u.id = ut.usersid"
            + "WHERE ut.teamsid = ?1",
            nativeQuery = true
    )
    List<User> findUsersByTeam(Long teamid);

    @Query(
            value = "SELECT t.* FROM teams t"
            + "JOIN users_teams ut ON t.id = ut.teamsid"
            + "WHERE ut.usersid = ?1",
            nativeQuery = true
    )
    List<Team> findTeamsByUser(Long userid);
}
