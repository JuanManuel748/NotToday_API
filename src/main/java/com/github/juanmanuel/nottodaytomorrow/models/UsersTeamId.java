package com.github.juanmanuel.nottodaytomorrow.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class UsersTeamId implements java.io.Serializable {
    private static final long serialVersionUID = -3951977874939903974L;
    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UsersTeamId entity = (UsersTeamId) o;
        return Objects.equals(this.teamId, entity.teamId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamId, userId);
    }

    public UsersTeamId() {
    }

    public UsersTeamId(Long userId, Long teamId) {
        this.userId = userId;
        this.teamId = teamId;
    }

    public UsersTeamId(User user, Team team) {
        this.userId = user.getId();
        this.teamId = team.getId();
    }

}