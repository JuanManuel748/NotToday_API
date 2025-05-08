package com.github.juanmanuel.nottodaytomorrow.services;

import com.github.juanmanuel.nottodaytomorrow.models.Team;
import com.github.juanmanuel.nottodaytomorrow.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;

    public Team create(Team team) {
        return teamRepository.save(team);
    }

    public Team update(Long id, Team team) {
        if (teamRepository.findById(id).isPresent()) {
            team.setId(id);
            return teamRepository.save(team);
        } else {
            throw new RuntimeException("Team not found with id: " + id);
        }
    }

    public boolean delete(Long id) {
        if (teamRepository.existsById(id)) {
            teamRepository.deleteById(id);
            return true;
        } else {
            throw new RuntimeException("Team not found with id: " + id);
        }
    }

    public Team getById(Long id) {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isPresent()) {
            return team.get();
        } else {
            throw new RuntimeException("Team not found with id: " + id);
        }
    }

    public List<Team> getAll() {
        return teamRepository.findAll();
    }
}
