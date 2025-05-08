package com.github.juanmanuel.nottodaytomorrow.services;

import com.github.juanmanuel.nottodaytomorrow.exceptions.NotFoundException;
import com.github.juanmanuel.nottodaytomorrow.models.Team;
import com.github.juanmanuel.nottodaytomorrow.models.User;
import com.github.juanmanuel.nottodaytomorrow.models.UsersTeam;
import com.github.juanmanuel.nottodaytomorrow.repositories.UserRepository;
import com.github.juanmanuel.nottodaytomorrow.repositories.UsersTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private TeamService teamService;
    @Autowired
    private UsersTeamService usersTeamService;

    public List<User> getAll() {
        return userRepo.findAll();
    }


    public User getById(Long id) throws NotFoundException {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NotFoundException("User not found with id: " + id, user);
        }
    }

    public User getByIdwTeams(Long id) throws NotFoundException {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            // Fetch the teams associated with the user



            return user.get();
        } else {
            throw new NotFoundException("User not found with id: " + id, user);
        }
    }



    public User getByEmail(String email) throws NotFoundException {
        Optional<User> user = userRepo.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NotFoundException("User not found with email: " + email, user);
        }
    }

    public List<User> getByName(String name) throws NotFoundException {
        List<User> users = userRepo.findByName(name);
        if (users.isEmpty()) {
            throw new NotFoundException("User not found with name: " + name, users);
        } else {
            return users;
        }
    }

    public User create(User user) {
        return userRepo.save(user);
    }

    public User update(Long id, User user) {
        Optional<User> existingUser = userRepo.findById(id);
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPassword(user.getPassword());
            updatedUser.setName(user.getName());
            updatedUser.setPic(user.getPic());
            updatedUser.setDescription(user.getDescription());
            updatedUser.setArea(user.getArea());
            return userRepo.save(updatedUser);
        } else {
            throw new NotFoundException("User not found with id: " + user.getId(), existingUser);
        }
    }

    public boolean delete(Long id) {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            userRepo.delete(user.get());
            return true;
        } else {
            throw new NotFoundException("User not found with id: " + id, user);
        }
    }

    public List<Team> getTeamsByUserEmail(String email) {
        User user = getByEmail(email);
        List<Team> teams = usersTeamService.getTeamsByUserId(user.getId());
        if (!teams.isEmpty()) {
            return teams;
        } else {
            throw new NotFoundException("No teams found for user: " + email, user);
        }

    }

    public User addTeamToUser(Long userId, Long teamId, String role) throws NotFoundException {
        User user = getById(userId);
        if (user != null) {
            Team team = teamService.getById(teamId);
            if (team != null) {
                UsersTeam ut = usersTeamService.create(new UsersTeam(user, team, role));
                if (ut != null) {
                    if (user.getUsersTeams().isEmpty()) {
                        user.setUsersTeams(new ArrayList<>());
                    }
                    user.getUsersTeams().add(ut);
                }
            } else {
                throw new NotFoundException("Team not found with id: " + teamId, team);
            }
            return user;
        } else {
            throw new NotFoundException("User not found with id: " + userId, user);
        }

    }


}
