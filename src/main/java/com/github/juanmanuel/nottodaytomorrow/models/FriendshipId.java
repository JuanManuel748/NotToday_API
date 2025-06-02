package com.github.juanmanuel.nottodaytomorrow.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FriendshipId implements Serializable {

    @Column(name = "user_id1")
    private Long userId1;

    @Column(name = "user_id2")
    private Long userId2;

    public FriendshipId() {}

    public FriendshipId(Long userId1, Long userId2) {
        // Asegurar el orden para la clave compuesta si se usa la restricción CHECK (user_id1 < user_id2)
        if (userId1 < userId2) {
            this.userId1 = userId1;
            this.userId2 = userId2;
        } else {
            this.userId1 = userId2;
            this.userId2 = userId1;
        }
    }

    // Getters, Setters, equals y hashCode

    public Long getUserId1() {
        return userId1;
    }

    public void setUserId1(Long userId1) {
        this.userId1 = userId1;
    }

    public Long getUserId2() {
        return userId2;
    }

    public void setUserId2(Long userId2) {
        this.userId2 = userId2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendshipId that = (FriendshipId) o;
        return Objects.equals(userId1, that.userId1) && Objects.equals(userId2, that.userId2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId1, userId2);
    }
}