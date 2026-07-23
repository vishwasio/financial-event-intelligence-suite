package io.vishwasio.feis.trafficgenerator.generation.domain;

import java.util.Objects;
import java.util.UUID;

public record PaymentRequestId(UUID value) {

    public PaymentRequestId {
        Objects.requireNonNull(value, "value must not be null");
    }

    public static PaymentRequestId generate() {
        return new PaymentRequestId(UUID.randomUUID());
    }

    public static PaymentRequestId of(UUID value) {
        return new PaymentRequestId(value);
    }
}
