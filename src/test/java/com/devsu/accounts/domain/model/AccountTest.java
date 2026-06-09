package com.devsu.accounts.domain.model;

import com.devsu.accounts.domain.exception.BusinessRuleViolationException;
import com.devsu.accounts.domain.exception.InvalidDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Account domain entity")
class AccountTest {

    private static final String VALID_ACCOUNT_NUMBER = "ACC-001";
    private static final String VALID_CUSTOMER_ID = "JLEMA";
    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("1000.00");

    private Account newActiveSavingsAccount() {
        return new Account(
                VALID_ACCOUNT_NUMBER,
                AccountType.SAVINGS,
                INITIAL_BALANCE,
                true,
                VALID_CUSTOMER_ID
        );
    }

    @Test
    @DisplayName("should create a valid account with initial balance equal to current balance")
    void shouldCreateValidAccount() {
        Account account = newActiveSavingsAccount();

        assertThat(account.getAccountNumber()).isEqualTo(VALID_ACCOUNT_NUMBER);
        assertThat(account.getAccountType()).isEqualTo(AccountType.SAVINGS);
        assertThat(account.getCurrentBalance()).isEqualByComparingTo(INITIAL_BALANCE);
        assertThat(account.isActive()).isTrue();
    }

    @Test
    @DisplayName("should reject invalid data on construction")
    void shouldRejectInvalidData() {
        assertThatThrownBy(() -> new Account(
                "  ", AccountType.SAVINGS, INITIAL_BALANCE, true, VALID_CUSTOMER_ID
        )).isInstanceOf(InvalidDataException.class);

        assertThatThrownBy(() -> new Account(
                VALID_ACCOUNT_NUMBER, AccountType.SAVINGS,
                new BigDecimal("-100"), true, VALID_CUSTOMER_ID
        )).isInstanceOf(InvalidDataException.class);
    }

    @Test
    @DisplayName("apply(deposit) should increase the balance and snapshot it on the movement")
    void shouldApplyDeposit() {
        Account account = newActiveSavingsAccount();
        Movement deposit = new Movement(MovementType.DEPOSIT, new BigDecimal("500.00"));

        account.apply(deposit);

        assertThat(account.getCurrentBalance()).isEqualByComparingTo("1500.00");
        assertThat(deposit.getBalance()).isEqualByComparingTo("1500.00");
    }

    @Test
    @DisplayName("apply(withdrawal) should decrease the balance")
    void shouldApplyWithdrawal() {
        Account account = newActiveSavingsAccount();
        Movement withdrawal = new Movement(MovementType.WITHDRAWAL, new BigDecimal("400.00"));

        account.apply(withdrawal);

        assertThat(account.getCurrentBalance()).isEqualByComparingTo("600.00");
    }

    @Test
    @DisplayName("apply(withdrawal) should allow leaving balance at exactly zero")
    void shouldAllowWithdrawalToZero() {
        Account account = newActiveSavingsAccount();
        Movement withdrawal = new Movement(MovementType.WITHDRAWAL, INITIAL_BALANCE);

        account.apply(withdrawal);

        assertThat(account.getCurrentBalance()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("apply(withdrawal) should reject insufficient balance with 'Saldo no disponible'")
    void shouldRejectInsufficientBalance() {
        Account account = newActiveSavingsAccount();
        Movement withdrawal = new Movement(MovementType.WITHDRAWAL, new BigDecimal("1500.00"));

        assertThatThrownBy(() -> account.apply(withdrawal))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessage("Saldo no disponible")
                .extracting(ex -> ((BusinessRuleViolationException) ex).getRuleCode())
                .isEqualTo("INSUFFICIENT_BALANCE");
    }

    @Test
    @DisplayName("apply should not modify the balance when rejected")
    void shouldNotModifyBalanceOnRejection() {
        Account account = newActiveSavingsAccount();
        Movement invalidWithdrawal = new Movement(MovementType.WITHDRAWAL, new BigDecimal("5000.00"));

        assertThatThrownBy(() -> account.apply(invalidWithdrawal))
                .isInstanceOf(BusinessRuleViolationException.class);

        assertThat(account.getCurrentBalance()).isEqualByComparingTo(INITIAL_BALANCE);
    }

    @Test
    @DisplayName("apply should reject movements on inactive accounts")
    void shouldRejectApplyOnInactiveAccount() {
        Account account = newActiveSavingsAccount();
        account.deactivate();
        Movement deposit = new Movement(MovementType.DEPOSIT, new BigDecimal("100"));

        assertThatThrownBy(() -> account.apply(deposit))
                .isInstanceOf(BusinessRuleViolationException.class)
                .extracting(ex -> ((BusinessRuleViolationException) ex).getRuleCode())
                .isEqualTo("ACCOUNT_INACTIVE");
    }

    @Test
    @DisplayName("deactivate should turn an active account inactive")
    void shouldDeactivateAccount() {
        Account account = newActiveSavingsAccount();

        account.deactivate();

        assertThat(account.isActive()).isFalse();
    }

    @Test
    @DisplayName("deactivate should fail on an already inactive account")
    void shouldRejectDoubleDeactivation() {
        Account account = newActiveSavingsAccount();
        account.deactivate();

        assertThatThrownBy(account::deactivate)
                .isInstanceOf(BusinessRuleViolationException.class)
                .extracting(ex -> ((BusinessRuleViolationException) ex).getRuleCode())
                .isEqualTo("ACCOUNT_ALREADY_INACTIVE");
        }
}