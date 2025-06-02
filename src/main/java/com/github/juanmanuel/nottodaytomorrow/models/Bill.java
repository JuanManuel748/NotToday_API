package com.github.juanmanuel.nottodaytomorrow.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "bills")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Size(max = 255)
    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "payer_id", nullable = false)
    private User payer;

    @NotNull
    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @OneToMany(mappedBy = "bill")
    @JsonManagedReference
    private List<BillsUser> billsUsers = new ArrayList<>();

    public Bill() {}

    public Bill(Long id) {
        this.id = id;
    }

    public Bill(BigDecimal amount, String description, Team team, User payer) {
        this.amount = amount;
        this.description = description;
        this.team = team;
        this.payer = payer;
        this.createdAt = LocalDate.now();
    }

    public Bill(BigDecimal amount, String description, Team team, User payer, LocalDate createdAt) {
        this.amount = amount;
        this.description = description;
        this.team = team;
        this.payer = payer;
        this.createdAt = createdAt;
    }

    public Bill(Long id, BigDecimal amount, String description, Team team, User payer) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.team = team;
        this.payer = payer;
        this.createdAt = LocalDate.now();
    }

    public Bill(Long id, BigDecimal amount, String description, Team team, User payer, LocalDate createdAt) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.team = team;
        this.payer = payer;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public User getPayer() {
        return payer;
    }

    public void setPayer(User payer) {
        this.payer = payer;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public List<BillsUser> getBillsUsers() {
        return billsUsers;
    }

    public void setBillsUsers(List<BillsUser> billsUsers) {
        this.billsUsers = billsUsers;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", team=" + team.getId() +
                ", payer=" + payer.getId() +
                ", createdAt=" + createdAt +
                ", billsUsers=" + (billsUsers != null ? billsUsers.size() : 0) +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Bill bill = (Bill) obj;
        return id != null && id.equals(bill.id);
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (createdAt == null) {
            createdAt = LocalDate.now();
        }
    }
}