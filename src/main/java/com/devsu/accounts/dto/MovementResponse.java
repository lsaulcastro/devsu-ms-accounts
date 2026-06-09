package com.devsu.accounts.dto;

import com.devsu.accounts.domain.model.MovementType;

import java.math.BigDecimal;
import java.time.Instant;

public record MovementResponse(
        Long id,
        Long accountId,
        MovementType movementType,
        BigDecimal amount,
        BigDecimal balance,
        Instant date
) {
}