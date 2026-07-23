package io.vishwasio.feis.trafficgenerator.generation.domain.event;

import io.vishwasio.feis.trafficgenerator.generation.domain.BankAccountId;
import io.vishwasio.feis.trafficgenerator.generation.domain.CustomerId;

import java.time.Instant;
import java.util.Objects;

public record BankAccountCreated(BankAccountId bankAccountId, CustomerId ownerId,
                                 String bankIdentifier, Instant occurredAt) implements GenerationEvent {

    public BankAccountCreated {
        Objects.requireNonNull(bankAccountId, "bankAccountId must not be null");
        Objects.requireNonNull(ownerId, "ownerId must not be null");
        Objects.requireNonNull(bankIdentifier, "bankIdentifier must not be null");
        Objects.requireNonNull(occurredAt, "occurredAt must not be null");
    }
}
