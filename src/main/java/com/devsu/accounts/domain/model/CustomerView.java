package com.devsu.accounts.domain.model;

import com.devsu.accounts.domain.validation.DomainValidator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Objects;


@Entity
@Table(name = "customer_view")
@Getter
@NoArgsConstructor
public class CustomerView {

    @Id
    @Column(name = "customer_id", length = 30)
    private String customerId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "identification", length = 20)
    private String identification;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "synced_at", nullable = false)
    private Instant syncedAt;

    public CustomerView(String customerId, String name, String identification, boolean active) {
        DomainValidator.requireNotBlank(customerId, "Customer ID");
        DomainValidator.requireNotBlank(name, "Name");

        this.customerId = customerId;
        this.name = name;
        this.identification = identification;
        this.active = active;
        this.syncedAt = Instant.now();
    }

    /**
     * Updates the view with new data from an event.
     * Called by the Kafka consumer when an UPDATED event arrives.
     */
    public void updateFrom(String name, String identification, boolean active) {
        DomainValidator.requireNotBlank(name, "Name");
        this.name = name;
        this.identification = identification;
        this.active = active;
        this.syncedAt = Instant.now();
    }

    /**
     * Marks this view as deactivated.
     * Called by the Kafka consumer when a DEACTIVATED event arrives.
     */
    public void markDeactivated() {
        this.active = false;
        this.syncedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerView that)) return false;
        return Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }
}