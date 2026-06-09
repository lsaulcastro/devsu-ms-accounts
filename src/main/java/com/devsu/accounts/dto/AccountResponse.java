package com.devsu.accounts.dto;

import com.devsu.accounts.domain.model.AccountType;

import java.math.BigDecimal;

public record AccountResponse(
        Long id,
        String accountNumber,
        AccountType accountType,
        BigDecimal initialBalance,
        BigDecimal currentBalance,
        boolean active,
        String customerId
) {
}