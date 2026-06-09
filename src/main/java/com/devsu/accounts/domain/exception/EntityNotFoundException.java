package com.devsu.accounts.domain.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends DomainException {

    private final String entityType;
    private final String entityId;

    public EntityNotFoundException(String entityType, String entityId) {
        super(String.format("%s with id '%s' was not found", entityType, entityId));
        this.entityType = entityType;
        this.entityId = entityId;
    }
}