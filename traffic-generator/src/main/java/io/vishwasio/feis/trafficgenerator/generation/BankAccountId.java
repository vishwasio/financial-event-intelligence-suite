package io.vishwasio.feis.trafficgenerator.generation;

import java.util.Objects;
import java.util.UUID;

/**
 * Identity of a {@link BankAccount}.
 */
public record BankAccountId(UUID value) {

    public BankAccountId {
        Objects.requireNonNull(value, "value must not be null");
    }

    public static BankAccountId generate() {
        return new BankAccountId(UUID.randomUUID());
    }

    public static BankAccountId of(UUID value) {
        return new BankAccountId(value);
    }
}
