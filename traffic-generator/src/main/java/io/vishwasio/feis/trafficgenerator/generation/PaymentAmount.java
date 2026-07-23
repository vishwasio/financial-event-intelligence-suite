package io.vishwasio.feis.trafficgenerator.generation;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * The monetary amount of a {@link PaymentRequest}.
 */
public record PaymentAmount(BigDecimal value, String currencyCode) {

    public PaymentAmount {
        Objects.requireNonNull(value, "value must not be null");
        Objects.requireNonNull(currencyCode, "currencyCode must not be null");
        if (value.signum() <= 0) {
            throw new IllegalArgumentException("value must be positive");
        }
        if (currencyCode.isBlank()) {
            throw new IllegalArgumentException("currencyCode must not be blank");
        }
    }

    public static PaymentAmount of(BigDecimal value, String currencyCode) {
        return new PaymentAmount(value, currencyCode);
    }
}
