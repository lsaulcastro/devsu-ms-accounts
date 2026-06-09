package com.devsu.accounts.domain.model;

import com.devsu.accounts.domain.validation.DomainValidator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "movements")
@Getter
@NoArgsConstructor
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "movement_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private MovementType movementType;

    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "balance", precision = 19, scale = 4)
    private BigDecimal balance;

    @Column(name = "date", nullable = false)
    private Instant date;

    public Movement(MovementType movementType, BigDecimal amount) {
        DomainValidator.requireNotNull(movementType, "Movement type");
        DomainValidator.requirePositive(amount, "Amount");

        this.movementType = movementType;
        this.amount = amount;
        this.date = Instant.now();
    }

    void attachTo(Account account, BigDecimal resultingBalance) {
        DomainValidator.requireNotNull(account, "Account");
        this.accountId = account.getId();
        this.balance = resultingBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movement that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}