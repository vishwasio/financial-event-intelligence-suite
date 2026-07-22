package io.vishwasio.feis.trafficgenerator.generation;

import java.util.Objects;

/**
 * A simulated customer initiating payment activity.
 *
 * <p>Customers exist only within the simulated dataset produced by
 * the traffic generator — this is not a real customer record and
 * carries no PII by design; {@code displayName} is a generated label
 * for readability in generated evidence, not a real identity.
 */
public record Customer(CustomerId id, String displayName) {

    public Customer {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(displayName, "displayName must not be null");
        if (displayName.isBlank()) {
            throw new IllegalArgumentException("displayName must not be blank");
        }
    }

    public static Customer of(String displayName) {
        return new Customer(CustomerId.generate(), displayName);
    }
}
