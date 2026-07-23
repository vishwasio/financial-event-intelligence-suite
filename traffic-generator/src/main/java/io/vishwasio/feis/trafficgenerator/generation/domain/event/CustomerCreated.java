package io.vishwasio.feis.trafficgenerator.generation.domain.event;

import io.vishwasio.feis.trafficgenerator.generation.domain.CustomerId;

import java.time.Instant;
import java.util.Objects;

public record CustomerCreated(CustomerId customerId, String displayName, Instant occurredAt)
    implements GenerationEvent {

    public CustomerCreated {
        Objects.requireNonNull(customerId, "customerId must not be null");
        Objects.requireNonNull(displayName, "displayName must not be null");
        Objects.requireNonNull(occurredAt, "occurredAt must not be null");
    }
}
