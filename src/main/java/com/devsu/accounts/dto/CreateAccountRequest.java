package com.devsu.accounts.dto;

import com.devsu.accounts.domain.model.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateAccountRequest(

        @NotBlank(message = "Account number is required")
        @Size(max = 30, message = "Account number must not exceed 30 characters")
        String accountNumber,

        @NotNull(message = "Account type is required")
        AccountType accountType,

        @NotNull(message = "Initial balance is required")
        @DecimalMin(value = "0.00", inclusive = true, message = "Initial balance cannot be negative")
        @Digits(integer = 15, fraction = 4, message = "Initial balance must have at most 15 integer digits and 4 decimal places")
        BigDecimal initialBalance,

        @NotBlank(message = "Customer ID is required")
        @Size(max = 30, message = "Customer ID must not exceed 30 characters")
        String customerId
) {
}