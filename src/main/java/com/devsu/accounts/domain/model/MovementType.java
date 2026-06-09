package com.devsu.accounts.domain.model;

import java.math.BigDecimal;

public enum MovementType {
    DEPOSIT,
    WITHDRAWAL;

    public BigDecimal applyTo(BigDecimal amount) {
        return switch (this) {
            case DEPOSIT -> amount;
            case WITHDRAWAL -> amount.negate();
        };
    }

}