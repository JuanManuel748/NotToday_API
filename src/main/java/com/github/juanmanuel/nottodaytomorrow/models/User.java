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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public List<BillsUser> getBillsUsers() {
        return billsUsers;
    }

    public void setBillsUsers(List<BillsUser> billsUsers) {
        this.billsUsers = billsUsers;
    }

    public List<Comment> getMyComments() {
        return myComments;
    }

    public void setMyComments(List<Comment> myComments) {
        this.myComments = myComments;
    }

    public List<Comment> getCommentsMade() {
        return commentsMade;
    }

    public void setCommentsMade(List<Comment> commentsMade) {
        this.commentsMade = commentsMade;
    }

    public List<Task> getTasksCreated() {
        return tasksCreated;
    }

    public void setTasksCreated(List<Task> tasksCreated) {
        this.tasksCreated = tasksCreated;
    }

    public List<Task> getTasksAssigned() {
        return tasksAssigned;
    }

    public void setTasksAssigned(List<Task> tasksAssigned) {
        this.tasksAssigned = tasksAssigned;
    }

    public List<UsersTeam> getUsersTeams() {
        return usersTeams;
    }

    public void setUsersTeams(List<UsersTeam> usersTeams) {
        this.usersTeams = usersTeams;
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