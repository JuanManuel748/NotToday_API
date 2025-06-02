package com.github.juanmanuel.nottodaytomorrow.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "friendships")
public class Friendship {

    @EmbeddedId
    private FriendshipId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId1")
    @JoinColumn(name = "user_id1")
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId2")
    @JoinColumn(name = "user_id2")
    private User user2;

    @NotNull
    @Size(max = 50)
    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Friendship() {
        this.createdAt = LocalDateTime.now();
    }

    public Friendship(User user1, User user2, String status) {
        this.id = new FriendshipId(user1.getId(), user2.getId());
        this.user1 = user1;
        this.user2 = user2;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public Friendship(Long user1, Long user2, String status) {
        this.id = new FriendshipId(user1, user2);
        this.user1 = new User(user1);
        this.user2 = new User(user2);
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public Friendship(FriendshipId id, User user1, User user2, String status) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public Friendship(FriendshipId id, String status) {
        this.id = id;
        this.user1 = new User(id.getUserId1());
        this.user2 = new User(id.getUserId2());
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public FriendshipId getId() {
        return id;
    }

    public void setId(FriendshipId id) {
        this.id = id;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}