package com.github.juanmanuel.nottodaytomorrow.services;

import com.github.juanmanuel.nottodaytomorrow.exceptions.NotFoundException;
import com.github.juanmanuel.nottodaytomorrow.models.Team;
import com.github.juanmanuel.nottodaytomorrow.models.User;
import com.github.juanmanuel.nottodaytomorrow.models.UsersTeam;
import com.github.juanmanuel.nottodaytomorrow.repositories.UsersTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsersTeamService {
    @Autowired
    private UsersTeamRepository usersTeamRepository;

    public List<User> getUsersByTeamId(Long teamId) {
        List<User> users = usersTeamRepository.findUsersByTeam(teamId);
        if (!users.isEmpty()) {
            return users;
        } else {
            throw new NotFoundException("No users found for team with id: " + teamId, users);
        }
    }

    public List<Team> getTeamsByUserId(Long userId) throws NotFoundException {
        List<Team> teams = usersTeamRepository.findTeamsByUser(userId);
        if (!teams.isEmpty()) {
            return teams;
        } else {
            throw new NotFoundException("No teams found for user with id: " + userId, teams);
        }
    }



    public UsersTeam getByUserAndTeam(Long userId, Long teamId) throws NotFoundException {
        Optional<UsersTeam> usersTeam = usersTeamRepository.findByUserAndTeam(userId, teamId);
        if (usersTeam.isPresent()) {
            return usersTeam.get();
        } else {
            throw new NotFoundException("No association found between user with id: " + userId + " and team with id: " + teamId, usersTeam);
        }
    }
    public List<UsersTeam> getByUserId(Long userId) {
        List<UsersTeam> usersTeams = usersTeamRepository.findByUser(userId);
        if (!usersTeams.isEmpty()) {
            return usersTeams;
        } else {
            throw new NotFoundException("No associations found for user with id: " + userId, usersTeams);
        }
    }

    public List<UsersTeam> getByTeamId(Long teamId) {
        List<UsersTeam> usersTeams = usersTeamRepository.findByTeam(teamId);
        if (!usersTeams.isEmpty()) {
            return usersTeams;
        } else {
            throw new NotFoundException("No associations found for team with id: " + teamId, usersTeams);
        }
    }

    public List<UsersTeam> getAll() {
        return usersTeamRepository.findAll();
    }

    public UsersTeam create(UsersTeam usersTeam) {
        return usersTeamRepository.save(usersTeam);
    }

    public UsersTeam update(Long userId, Long teamId, UsersTeam updatedUsersTeam) throws NotFoundException {
        Optional<UsersTeam> existingUsersTeam = usersTeamRepository.findByUserAndTeam(userId, teamId);
        if (existingUsersTeam.isPresent()) {
            UsersTeam usersTeam = existingUsersTeam.get();
            usersTeam.setRole(updatedUsersTeam.getRole());
            return usersTeamRepository.save(usersTeam);
        } else {
            throw new NotFoundException("Association not found with id: " + userId + ", " + teamId, existingUsersTeam);
        }
    }

    public boolean delete(Long userId, Long teamId) {
        Optional<UsersTeam> usersTeam = usersTeamRepository.findByUserAndTeam(userId, teamId);
        if (usersTeam.isPresent()) {
            usersTeamRepository.delete(usersTeam.get());
            return true;
        } else {
            throw new NotFoundException("Association not found with id: " + userId + ", " + teamId, usersTeam);
        }
    }




}
