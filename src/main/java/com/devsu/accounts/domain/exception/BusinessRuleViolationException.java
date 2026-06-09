package com.devsu.accounts.domain.exception;

import lombok.Getter;

@Getter
public class BusinessRuleViolationException extends DomainException {

    private final String ruleCode;

    public BusinessRuleViolationException(String ruleCode, String message) {
        super(message);
        this.ruleCode = ruleCode;
    }
}