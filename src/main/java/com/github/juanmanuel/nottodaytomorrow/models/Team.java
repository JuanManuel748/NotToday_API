package com.github.juanmanuel.nottodaytomorrow.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "teams")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "image")
    private byte[] imagen;

    @NotNull
    @ColumnDefault("current_timestamp()")
    @Column(name = "creation_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate creationDate;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Bill> bills = new LinkedHashSet<>();

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Task> tasks = new LinkedHashSet<>();

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<UsersTeam> usersTeams = new LinkedHashSet<>();


    public Team() {}

    public Team(Long id) {
        this.id = id;
    }

    public Team(String name) {
        this.name = name;
    }

    public Team(Long id, String name, LocalDate creationDate, byte[] imagen) {
        this.id = id;
        this.name = name;
        this.imagen = imagen;
        this.creationDate = creationDate;
    }

    public Team(Long id, String name, byte[] imagen, LocalDate creationDate, Set<Bill> bills, Set<Task> tasks, Set<UsersTeam> usersTeams) {
        this.id = id;
        this.name = name;
        this.imagen = imagen;
        this.creationDate = creationDate;
        this.bills = bills;
        this.tasks = tasks;
        this.usersTeams = usersTeams;
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

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] image) {
        this.imagen = image;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Set<Bill> getBills() {
        return bills;
    }

    public void setBills(Set<Bill> bills) {
        this.bills = bills;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Set<UsersTeam> getUsersTeams() {
        return usersTeams;
    }

    public void setUsersTeams(Set<UsersTeam> usersTeams) {
        this.usersTeams = usersTeams;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image=" + (imagen != null ? "present" : "not present") +
                ", creationDate=" + creationDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(id, team.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}