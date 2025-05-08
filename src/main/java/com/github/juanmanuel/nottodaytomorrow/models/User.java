package com.github.juanmanuel.nottodaytomorrow.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Setter
    @Size(max = 250)
    @NotNull
    @Column(name = "email", nullable = false, length = 250)
    private String email;

    @Setter
    @Size(max = 250)
    @NotNull
    @Column(name = "password", nullable = false, length = 250)
    private String password;

    @Setter
    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 255)
    @Column(name = "pic")
    private String pic;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 255)
    @Column(name = "area")
    private String area;

    @OneToMany(mappedBy = "payer", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Bill> bills = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BillsUser> billsUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Comment> myComments = new ArrayList<>();

    @OneToMany(mappedBy = "commenter", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Comment> commentsMade = new ArrayList<>();

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Task> tasksCreated = new ArrayList<>();

    @OneToMany(mappedBy = "assigned", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Task> tasksAssigned = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UsersTeam> usersTeams = new ArrayList<>();


    public User() {}

    public User(Long id) {
        this.id = id;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User(String email, String password, String name, String pic, String description, String area) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.pic = pic;
        this.description = description;
        this.area = area;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", pic='" + pic + '\'' +
                ", description='" + description + '\'' +
                ", area='" + area + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;

        if (getId() != null && user.getId() != null && getId().equals(user.getId())) return true;
        return getEmail() != null && user.getEmail() != null && getEmail().equals(user.getEmail());
    }
}