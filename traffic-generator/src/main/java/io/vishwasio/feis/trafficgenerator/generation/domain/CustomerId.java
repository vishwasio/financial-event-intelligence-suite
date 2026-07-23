package io.vishwasio.feis.trafficgenerator.generation.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Identity of a {@link Customer}.
 */
public record CustomerId(UUID value) {

    public CustomerId {
        Objects.requireNonNull(value, "value must not be null");
    }

    public static CustomerId generate() {
        return new CustomerId(UUID.randomUUID());
    }

    public static CustomerId of(UUID value) {
        return new CustomerId(value);
    }
}
