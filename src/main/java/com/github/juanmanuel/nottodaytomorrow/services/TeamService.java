package com.github.juanmanuel.nottodaytomorrow.services;

import com.github.juanmanuel.nottodaytomorrow.exceptions.NotFoundException;
import com.github.juanmanuel.nottodaytomorrow.models.Team;
import com.github.juanmanuel.nottodaytomorrow.models.User;
import com.github.juanmanuel.nottodaytomorrow.repositories.TeamRepository;
import com.github.juanmanuel.nottodaytomorrow.repositories.UsersTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UsersTeamRepository usrtmRepository;

    public Team create(Team team) {
        return teamRepository.save(team);
    }

    public Team update(Long id, Team team) throws NotFoundException {
        if (teamRepository.findById(id).isPresent()) {
            team.setId(id);
            return teamRepository.save(team);
        } else {
            throw new RuntimeException("Team not found with id: " + id);
        }
    }

    public boolean delete(Long id) throws NotFoundException {
        if (teamRepository.existsById(id)) {
            teamRepository.deleteById(id);
            return true;
        } else {
            throw new RuntimeException("Team not found with id: " + id);
        }
    }

    public Team getById(Long id) throws NotFoundException {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isPresent()) {
            return team.get();
        } else {
            throw new RuntimeException("Team not found with id: " + id);
        }
    }

    public List<Team> getAll() throws NotFoundException {
        List<Team> teams = teamRepository.findAll();
        if (!teams.isEmpty()) {
            return teams;
        } else {
            throw new NotFoundException("No teams found", Team.class);
        }
    }

    public List<User> getUsersByTeamId(Long id) throws NotFoundException {
        List<User> users = usrtmRepository.findUsersByTeam(id);
        if (!users.isEmpty()) {
            return users;
        } else {
            throw new NotFoundException("No users found for team with id: " + id, users);
        }
    }
}
