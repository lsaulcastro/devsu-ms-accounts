package com.devsu.accounts.domain.model;

import com.devsu.accounts.domain.exception.BusinessRuleViolationException;
import com.devsu.accounts.domain.validation.DomainValidator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "accounts")
@Getter
@NoArgsConstructor
public class Account {

    private static final int ACCOUNT_NUMBER_MAX_LENGTH = 30;
    private static final int CUSTOMER_ID_MAX_LENGTH = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false, unique = true, length = ACCOUNT_NUMBER_MAX_LENGTH)
    private String accountNumber;

    @Column(name = "account_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "initial_balance", nullable = false, precision = 19, scale = 4)
    private BigDecimal initialBalance;

    @Column(name = "current_balance", nullable = false, precision = 19, scale = 4)
    private BigDecimal currentBalance;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "customer_id", nullable = false, length = CUSTOMER_ID_MAX_LENGTH)
    private String customerId;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public Account(String accountNumber, AccountType accountType,
                   BigDecimal initialBalance, boolean active, String customerId) {
        validateAccountNumber(accountNumber);
        validateCustomerId(customerId);
        DomainValidator.requireNotNull(accountType, "Account type");
        DomainValidator.requireNonNegative(initialBalance, "Initial balance");

        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.initialBalance = initialBalance;
        this.currentBalance = initialBalance;
        this.active = active;
        this.customerId = customerId;
    }

    public void apply(Movement movement) {
        DomainValidator.requireNotNull(movement, "Movement");
        ensureActive();

        BigDecimal signedDelta = movement.getMovementType().applyTo(movement.getAmount());
        BigDecimal newBalance = this.currentBalance.add(signedDelta);

        if (newBalance.signum() < 0) {
            throw new BusinessRuleViolationException(
                    "INSUFFICIENT_BALANCE",
                    "Saldo no disponible");
        }

        this.currentBalance = newBalance;
        movement.attachTo(this, newBalance);
    }

    public void updateData(AccountType accountType, boolean active) {
        ensureActive();
        DomainValidator.requireNotNull(accountType, "Account type");
        this.accountType = accountType;
        this.active = active;
    }

    public void deactivate() {
        if (!this.active) {
            throw new BusinessRuleViolationException(
                    "ACCOUNT_ALREADY_INACTIVE",
                    "Account " + accountNumber + " is already inactive");
        }
        this.active = false;
    }

    public void activate() {
        if (this.active) {
            throw new BusinessRuleViolationException(
                    "ACCOUNT_ALREADY_ACTIVE",
                    "Account " + accountNumber + " is already active");
        }
        this.active = true;
    }

    private void ensureActive() {
        if (!this.active) {
            throw new BusinessRuleViolationException(
                    "ACCOUNT_INACTIVE",
                    "Account " + accountNumber + " is inactive and cannot operate");
        }
    }

    private void validateAccountNumber(String accountNumber) {
        DomainValidator.requireNotBlank(accountNumber, "Account number");
        DomainValidator.requireMaxLength(accountNumber, ACCOUNT_NUMBER_MAX_LENGTH, "Account number");
    }

    private void validateCustomerId(String customerId) {
        DomainValidator.requireNotBlank(customerId, "Customer ID");
        DomainValidator.requireMaxLength(customerId, CUSTOMER_ID_MAX_LENGTH, "Customer ID");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account that)) return false;
        return Objects.equals(accountNumber, that.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}