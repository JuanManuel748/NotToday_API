package com.github.juanmanuel.nottodaytomorrow.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "tasks")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "limit_date", nullable = false)
    private LocalDate limitDate;

    @NotNull
    @Column(name = "limit_hour", nullable = false)
    private LocalTime limitHour;

    @Size(max = 100)
    @NotNull
    @Column(name = "state", nullable = false, length = 100)
    private String state;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "assigned_id", nullable = false)
    private User assigned;

    @NotNull
    @ColumnDefault("current_timestamp()")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;


    public Task() {}

    public Task(Long id) {
        this.id = id;
    }

    public Task(Long id, String name, String description, LocalDate date, LocalTime hour, Long teamId, Long creatorId, Long assignedId) {
        this.name = name;
        this.description = description;
        this.limitDate = date;
        this.limitHour = hour;
        this.state = "PENDING";
        this.team = new Team(teamId);
        this.creator = new User(creatorId);
        this.assigned = new User(assignedId);
        this.createdAt = LocalDate.now();
    }
    public Task(Long id, String name, String description, LocalDate date, LocalTime hour, String state, Long teamId, Long creatorId, Long assignedId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.limitDate = date;
        this.limitHour = hour;
        this.state = state;
        this.team = new Team(teamId);
        this.creator = new User(creatorId);
        this.assigned = new User(assignedId);
        this.createdAt = LocalDate.now();
    }

    public Task(Long id, String name, String description, LocalDate date, LocalTime hour, Long teamId, Long creatorId, Long assignedId, LocalDate createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.limitDate = date;
        this.limitHour = hour;
        this.state = "PENDING";
        this.team = new Team(teamId);
        this.creator = new User(creatorId);
        this.assigned = new User(assignedId);
        this.createdAt = createdAt;
    }
    public Task(Long id, String name, String description, LocalDate date, LocalTime hour, String state, Long teamId, Long creatorId, Long assignedId, LocalDate createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.limitDate = date;
        this.limitHour = hour;
        this.state = state;
        this.team = new Team(teamId);
        this.creator = new User(creatorId);
        this.assigned = new User(assignedId);
        this.createdAt = createdAt;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(LocalDate limitDate) {
        this.limitDate = limitDate;
    }

    public LocalTime getLimitHour() {
        return limitHour;
    }

    public void setLimitHour(LocalTime limitHour) {
        this.limitHour = limitHour;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getAssigned() {
        return assigned;
    }

    public void setAssigned(User assigned) {
        this.assigned = assigned;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", limitDate=" + limitDate +
                ", limitHour=" + limitHour +
                ", state='" + state + '\'' +
                ", team=" + team.getId() +
                ", creator=" + creator.getId() +
                ", assigned=" + assigned.getId() +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;

        return task.getId().equals(this.id);
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDate.now();
        }
    }

}