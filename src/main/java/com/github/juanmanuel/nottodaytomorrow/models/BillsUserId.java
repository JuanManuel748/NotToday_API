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
public class BillsUserId implements java.io.Serializable {
    private static final long serialVersionUID = -2233677811368431409L;
    @NotNull
    @Column(name = "bill_id", nullable = false)
    private Long billId;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    public BillsUserId() {
    }

    public BillsUserId(Long billId, Long userId) {
        this.billId = billId;
        this.userId = userId;
    }

    public BillsUserId(Bill bill, User user) {
        this.billId = bill.getId();
        this.userId = user.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BillsUserId entity = (BillsUserId) o;
        return Objects.equals(this.billId, entity.billId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(billId, userId);
    }

}