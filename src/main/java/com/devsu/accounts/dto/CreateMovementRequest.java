package com.devsu.accounts.dto;

import com.devsu.accounts.domain.model.MovementType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateMovementRequest(

        @NotNull(message = "Movement type is required")
        MovementType movementType,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than zero")
        @Digits(integer = 15, fraction = 4, message = "Amount must have at most 15 integer digits and 4 decimal places")
        BigDecimal amount
) {
}