package com.github.juanmanuel.nottodaytomorrow.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "users_teams")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UsersTeam {
    @EmbeddedId
    private UsersTeamId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @MapsId("teamId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "team_id", nullable = false)
    @JsonIgnore
    private Team team;

    @Size(max = 45)
    @NotNull
    @Column(name = "role", nullable = false, length = 45)
    private String role;

    public UsersTeam() {}

    public UsersTeam(User u, Team t) {
        this.id = new UsersTeamId(u, t);
        this.user = u;
        this.team = t;
    }

    public UsersTeam(UsersTeamId id) {
        this.id = id;
        this.team = new Team(id.getTeamId());
        this.user = new User(id.getUserId());
    }

    public UsersTeam(UsersTeamId id, User user, Team team) {
        this.id = id;
        this.user = user;
        this.team = team;
    }

    public UsersTeam(User user, Team team, String role) {
        this.id = new UsersTeamId(user, team);
        this.user = user;
        this.team = team;
        this.role = role;
    }

    public UsersTeam(UsersTeamId id, String role) {
        this.id = id;
        this.team = new Team(id.getTeamId());
        this.user = new User(id.getUserId());
        this.role = role;
    }
    public UsersTeam(UsersTeamId id, User user, Team team, String role) {
        this.id = id;
        this.user = user;
        this.team = team;
        this.role = role;
    }

    public UsersTeamId getId() {
        return id;
    }

    public void setId(UsersTeamId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UsersTeam{" +
                "user=" + user +
                ", team=" + team +
                ", role='" + role + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsersTeam that)) return false;

        if (!getId().equals(that.getId())) return false;
        if (!getUser().equals(that.getUser())) return false;
        return getTeam().equals(that.getTeam());
    }
}