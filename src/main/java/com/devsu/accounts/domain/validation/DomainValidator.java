package com.devsu.accounts.domain.validation;

import com.devsu.accounts.domain.exception.InvalidDataException;

import java.math.BigDecimal;

public final class DomainValidator {

    private DomainValidator() {
    }

    public static void requireNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new InvalidDataException(fieldName + " is required");
        }
    }

    public static void requireNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidDataException(fieldName + " is required");
        }
    }

    public static void requireMaxLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength) {
            throw new InvalidDataException(
                    fieldName + " must not exceed " + maxLength + " characters");
        }
    }

    public static void requireNonNegative(BigDecimal value, String fieldName) {
        if (value == null) {
            throw new InvalidDataException(fieldName + " is required");
        }
        if (value.signum() < 0) {
            throw new InvalidDataException(fieldName + " cannot be negative");
        }
    }

    public static void requirePositive(BigDecimal value, String fieldName) {
        if (value == null) {
            throw new InvalidDataException(fieldName + " is required");
        }
        if (value.signum() <= 0) {
            throw new InvalidDataException(fieldName + " must be greater than zero");
        }
    }
}