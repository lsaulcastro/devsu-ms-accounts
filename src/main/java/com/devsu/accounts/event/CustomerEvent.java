package com.devsu.accounts.event;

import java.time.Instant;

public record CustomerEvent(
        CustomerEventType eventType,
        String customerId,
        String name,
        String identification,
        boolean active,
        Instant occurredAt
) {
}