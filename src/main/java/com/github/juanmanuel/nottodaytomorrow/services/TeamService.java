package com.github.juanmanuel.nottodaytomorrow.services;

import com.github.juanmanuel.nottodaytomorrow.exceptions.NotFoundException;
import com.github.juanmanuel.nottodaytomorrow.exceptions.ValidationException;
import com.github.juanmanuel.nottodaytomorrow.models.Team;
import com.github.juanmanuel.nottodaytomorrow.models.User;
import com.github.juanmanuel.nottodaytomorrow.repositories.TeamRepository;
import com.github.juanmanuel.nottodaytomorrow.repositories.UsersTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UsersTeamRepository usrtmRepository;

    private static final long MAX_IMAGE_SIZE_BYTES = 1024 * 1024; // 1MB para las imagenes

    public Team create(Team team) {
        if (team.getCreationDate() == null) {
            team.setCreationDate(LocalDate.now());
        }
        validateTeam(team);
        return teamRepository.save(team);
    }

    public Team update(Long id, Team team) throws NotFoundException {
        if (teamRepository.findById(id).isPresent()) {
            team.setId(id);
            validateTeam(team);
            return teamRepository.save(team);
        } else {
            throw new NotFoundException("Team not found with id: " + id, Team.class);
        }
    }

    public boolean delete(Long id) throws NotFoundException {
        if (teamRepository.existsById(id)) {
            teamRepository.deleteById(id);
            return true;
        } else {
            throw new NotFoundException("Team not found with id: " + id, Team.class);
        }
    }

    public Team getById(Long id) throws NotFoundException {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isPresent()) {
            return team.get();
        } else {
            throw new NotFoundException("Team not found with id: " + id, Team.class);
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

    public List<Team> getTeamsByUser(Long userId) throws NotFoundException {
        List<Team> teams = usrtmRepository.findTeamsByUser(userId);
        if (!teams.isEmpty()) {
            return teams;
        } else {
            throw new NotFoundException("No users found for team with id: " + userId, teams);
        }
    }

    public String getUserRoleInTeam(Long teamId, Long userId) throws NotFoundException {
        Optional<String> role = usrtmRepository.findRoleByUserAndTeam(userId, teamId);
        if (role.isPresent()) {
            return role.get();
        } else {
            throw new NotFoundException("No role found for user with id: " + userId + " in team with id: " + teamId, Team.class);
        }
    }

    public Team getByCode(String code) throws NotFoundException {
        Optional<Team> team = teamRepository.findByTeamCode(code);
        if (team.isPresent()) {
            return team.get();
        } else {
            throw new NotFoundException("Team not found with code: " + code, Team.class);
        }
    }

    private void validateTeam(Team team) throws ValidationException {
        if (team.getImagen() != null && team.getImagen().length > MAX_IMAGE_SIZE_BYTES) {
            double maxSizeMB = MAX_IMAGE_SIZE_BYTES / (1024.0 * 1024.0);
            double currentSizeMB = team.getImagen().length / (1024.0 * 1024.0);
            throw new ValidationException(
                    String.format("La imagen del equipo es demasiado grande. El tamaño máximo permitido es %.2f MB. Tamaño actual: %.2f MB.", maxSizeMB, currentSizeMB),
                    String.valueOf(team.getImagen().length) + " bytes",
                    team
            );
        }
    }
}
