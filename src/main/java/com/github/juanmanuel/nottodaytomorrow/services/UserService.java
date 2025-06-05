package com.github.juanmanuel.nottodaytomorrow.services;

import com.github.juanmanuel.nottodaytomorrow.exceptions.NotFoundException;
import com.github.juanmanuel.nottodaytomorrow.models.Team;
import com.github.juanmanuel.nottodaytomorrow.models.User;
import com.github.juanmanuel.nottodaytomorrow.models.UsersTeam;
import com.github.juanmanuel.nottodaytomorrow.repositories.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private TeamService teamService;
    @Autowired
    private UsersTeamService usersTeamService;

    private final PasswordEncoder passEncoder;

    @Autowired
    public UserService(UserRepository userRepo, TeamService teamService, UsersTeamService usersTeamService, @Lazy PasswordEncoder passEncoder) {
        this.userRepo = userRepo;
        this.teamService = teamService;
        this.usersTeamService = usersTeamService;
        this.passEncoder = passEncoder;
    }

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
        user.setPassword(passEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public User update(Long id, User user) throws NotFoundException {
        Optional<User> existingUserOptional = userRepo.findById(id);
        if (existingUserOptional.isPresent()) {
            User updatedUser = existingUserOptional.get();
            updatedUser.setEmail(user.getEmail());
            updatedUser.setName(user.getName());
            updatedUser.setPic(user.getPic());
            updatedUser.setDescription(user.getDescription());
            updatedUser.setArea(user.getArea());
            return userRepo.save(updatedUser);
        } else {
            throw new NotFoundException("User not found with id: " + id, User.class);
        }
    }

    public boolean delete(Long id) throws NotFoundException {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            userRepo.delete(user.get());
            return true;
        } else {
            throw new NotFoundException("User not found with id: " + id, user);
        }
    }


    public List<User> getUsersByTeamId(Long teamId) throws NotFoundException {
        List<User> users = usersTeamService.getUsersByTeamId(teamId);
        if (users.isEmpty()) {
            throw new NotFoundException("No users found for team with id: " + teamId, users);
        } else {
            return users;
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

    public User deleteTeam(Long userId, Long teamId) throws NotFoundException {
        User user = getById(userId);
        if (user != null) {
            Team team = teamService.getById(teamId);
            if (team != null) {
                UsersTeam ut = usersTeamService.getByUserAndTeam(user.getId(), team.getId());
                if (ut != null) {
                    usersTeamService.delete(user.getId(), team.getId());
                    user.getUsersTeams().remove(ut);
                }
            } else {
                throw new NotFoundException("Team not found with id: " + teamId, team);
            }
            return user;
        } else {
            throw new NotFoundException("User not found with id: " + userId, user);
        }

    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepo.findByEmail(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        User user = userOptional.get();
        // Por ahora, se asume que no hay roles/autoridades específicas.
        // Si tienes roles, deberías cargarlos aquí.
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>() // Lista vacía de autoridades
        );
    }

    public User changePassword(Long id, String currentPassword, String newPassword) throws NotFoundException {
        Optional<User> existingUserOptional = userRepo.findById(id);
        if (existingUserOptional.isPresent()) {
            User user = existingUserOptional.get();

            if (!passEncoder.matches(currentPassword, user.getPassword())) {
                throw new IllegalArgumentException("Current password is incorrect");
            }

            user.setPassword(passEncoder.encode(newPassword));
            return userRepo.save(user);
        } else {
            throw new NotFoundException("User not found with id: " + id, User.class);
        }
    }
}
