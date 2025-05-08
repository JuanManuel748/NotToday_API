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
public class CommentId implements java.io.Serializable {
    private static final long serialVersionUID = -8707671117390948655L;
    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "commenter_id", nullable = false)
    private Long commenterId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CommentId entity = (CommentId) o;
        return Objects.equals(this.userId, entity.userId) &&
                Objects.equals(this.commenterId, entity.commenterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, commenterId);
    }

}