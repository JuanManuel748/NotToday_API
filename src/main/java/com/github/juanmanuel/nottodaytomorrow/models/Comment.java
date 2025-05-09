package com.github.juanmanuel.nottodaytomorrow.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {
    @EmbeddedId
    private CommentId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("commenterId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "commenter_id", nullable = false)
    private User commenter;

    @NotNull
    @Column(name = "score", nullable = false)
    private Integer score;

    @Size(max = 255)
    @NotNull
    @Column(name = "comment", nullable = false)
    private String comment;

    @NotNull
    @Column(name = "comment_date", nullable = false)
    private LocalDate commentDate;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "anonymous", nullable = false)
    private Boolean anonymous = false;

    public Comment() {}

    public Comment(CommentId id) {
        this.id = id;
    }

    public Comment(User user, User com) {
        this.user = user;
        this.commenter = com;
        this.id = new CommentId(user.getId(), com.getId());
    }

    public Comment (Long u, Long c) {
        this.id = new CommentId(u, c);
        this.user = new User(u);
        this.commenter = new User(c);
    }

    public Comment(User user, User com, String comment, boolean anonymous) {
        this.user = user;
        this.commenter = com;
        this.comment = comment;
        this.anonymous = anonymous;
        this.id = new CommentId(user, com);
    }

    public Comment(Long user, Long com, int score, String comment, boolean anonymous) {
        this.user = new User(user);
        this.commenter = new User(com);
        this.score = score;
        this.comment = comment;
        this.anonymous = anonymous;
        this.id = new CommentId(user, com);
    }

    public Comment(User user, User com, int score, String comment, boolean anonymous) {
        this.user = user;
        this.commenter = com;
        this.score = score;
        this.comment = comment;
        this.anonymous = anonymous;
        this.id = new CommentId(user, com);
    }

    public Comment(Long user, Long com, String comment, boolean anonymous) {
        this.user = new User(user);
        this.commenter = new User(com);
        this.comment = comment;
        this.anonymous = anonymous;
        this.id = new CommentId(user, com);
    }

    public Comment(User user, User com, String comment, boolean anonymous, LocalDate created) {
        this.user = user;
        this.commenter = com;
        this.comment = comment;
        this.anonymous = anonymous;
        this.id = new CommentId(user, com);
        this.commentDate = created;
    }
    public Comment(Long user, Long com, String comment, boolean anonymous, LocalDate created) {
        this.user = new User(user);
        this.commenter = new User(com);
        this.comment = comment;
        this.anonymous = anonymous;
        this.id = new CommentId(user, com);
        this.commentDate = created;
    }

    public Comment(User user, User com, int score, String comment, boolean anonymous, LocalDate created) {
        this.user = user;
        this.commenter = com;
        this.score = score;
        this.comment = comment;
        this.anonymous = anonymous;
        this.id = new CommentId(user, com);
        this.commentDate = created;
    }
    public Comment(Long user, Long com, int score, String comment, boolean anonymous, LocalDate created) {
        this.user = new User(user);
        this.commenter = new User(com);
        this.score = score;
        this.comment = comment;
        this.anonymous = anonymous;
        this.id = new CommentId(user, com);
        this.commentDate = created;
    }

    public CommentId getId() {
        return id;
    }

    public void setId(CommentId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getCommenter() {
        return commenter;
    }

    public void setCommenter(User commenter) {
        this.commenter = commenter;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(LocalDate commentDate) {
        this.commentDate = commentDate;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", user=" + user.getId() +
                ", commenter=" + commenter.getId() +
                ", score=" + score +
                ", comment='" + comment + '\'' +
                ", commentDate=" + commentDate +
                ", anonymous=" + anonymous +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment1 = (Comment) o;
        return id.equals(comment1.id);
    }

}