package com.devsu.accounts.domain.model;

public enum AccountType {
    SAVINGS,
    CHECKING;

    public String toSpanish() {
        return switch (this) {
            case SAVINGS -> "Ahorro";
            case CHECKING -> "Corriente";
        };
    }
}