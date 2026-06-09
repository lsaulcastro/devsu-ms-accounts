package com.devsu.accounts.dto;

import com.devsu.accounts.domain.model.AccountType;
import jakarta.validation.constraints.NotNull;

public record UpdateAccountRequest(

        @NotNull(message = "Account type is required")
        AccountType accountType,

        @NotNull(message = "Active flag is required")
        Boolean active
) {
}