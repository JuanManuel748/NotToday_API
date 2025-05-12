package com.github.juanmanuel.nottodaytomorrow.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "bills_users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BillsUser {
    @EmbeddedId
    private BillsUserId id;

    @MapsId("billId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "owed", nullable = false, precision = 10, scale = 2)
    private BigDecimal owed;

    @ColumnDefault("0.00")
    @Column(name = "paid", precision = 10, scale = 2)
    private BigDecimal paid;

    @Column(name = "payment_date")
    private LocalDate paymentDate;


    public BillsUser() {
    }

    public BillsUser(Bill bill, User u) {
        this.bill = bill;
        this.user = u;
        this.id = new BillsUserId(bill, u);
    }

    public BillsUser(Bill bill, User u, BigDecimal owed) {
        this.bill = bill;
        this.user = u;
        this.id = new BillsUserId(bill, u);
        this.owed = owed;
        this.paid = BigDecimal.ZERO;
        this.paymentDate = null;
    }

    public BillsUser(Bill bill, User u, BigDecimal owed, BigDecimal paid) {
        this.bill = bill;
        this.user = u;
        this.id = new BillsUserId(bill, u);
        this.owed = owed;
        this.paid = paid;
        this.paymentDate = null;
    }

    public BillsUser(Bill bill, User u, BigDecimal owed, BigDecimal paid, LocalDate paymentDate) {
        this.bill = bill;
        this.user = u;
        this.id = new BillsUserId(bill, u);
        this.owed = owed;
        this.paid = paid;
        this.paymentDate = paymentDate;
    }

    // CON LONG
    public BillsUser(Long bill, Long u) {
        this.bill = new Bill(bill);
        this.user = new User(u);
        this.id = new BillsUserId(bill, u);
    }

    public BillsUser(Long bill, Long u, BigDecimal owed) {
        this.bill = new Bill(bill);
        this.user = new User(u);
        this.id = new BillsUserId(bill, u);
        this.owed = owed;
        this.paid = BigDecimal.ZERO;
        this.paymentDate = null;
    }

    public BillsUser(Long bill, Long u, BigDecimal owed, BigDecimal paid) {
        this.bill = new Bill(bill);
        this.user = new User(u);
        this.id = new BillsUserId(bill, u);
        this.owed = owed;
        this.paid = paid;
        this.paymentDate = null;
    }

    public BillsUser(Long bill, Long u, BigDecimal owed, BigDecimal paid, LocalDate paymentDate) {
        this.bill = new Bill(bill);
        this.user = new User(u);
        this.id = new BillsUserId(bill, u);
        this.owed = owed;
        this.paid = paid;
        this.paymentDate = paymentDate;
    }

    public BillsUserId getId() {
        return id;
    }

    public void setId(BillsUserId id) {
        this.id = id;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getOwed() {
        return owed;
    }

    public void setOwed(BigDecimal owed) {
        this.owed = owed;
    }

    public BigDecimal getPaid() {
        return paid;
    }

    public void setPaid(BigDecimal paid) {
        this.paid = paid;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }



    @Override
    public String toString() {
        return "BillsUser{" +
                "id=" + id +
                ", bill=" + bill.getId() +
                ", user=" + user.getId() +
                ", owed=" + owed +
                ", paid=" + paid +
                ", paymentDate=" + paymentDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BillsUser billsUser = (BillsUser) o;
        return Objects.equals(id, billsUser.id) && Objects.equals(bill, billsUser.bill) && Objects.equals(user, billsUser.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bill, user);
    }
}