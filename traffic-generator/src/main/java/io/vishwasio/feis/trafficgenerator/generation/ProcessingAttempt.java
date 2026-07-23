package io.vishwasio.feis.trafficgenerator.generation;

import java.time.Instant;
import java.util.Objects;

/**
 * An immutable record of one attempt at a single {@link ProcessingHop}
 * for a {@link PaymentRequest}.
 *
 * <p>The full sequence of attempts across a payment request's
 * lifetime is its evidence trail — this is what later becomes the
 * Kafka events, logs, and traces that financial-event-intelligence
 * investigates.
 */
public record ProcessingAttempt(ProcessingHop hop, AttemptOutcome outcome,
                                FailureReason failureReason, Instant occurredAt) {

    public ProcessingAttempt {
        Objects.requireNonNull(hop, "hop must not be null");
        Objects.requireNonNull(outcome, "outcome must not be null");
        Objects.requireNonNull(occurredAt, "occurredAt must not be null");
        if (outcome == AttemptOutcome.SUCCESS && failureReason != null) {
            throw new IllegalArgumentException("a successful attempt must not carry a failureReason");
        }
        if (outcome == AttemptOutcome.FAILURE && failureReason == null) {
            throw new IllegalArgumentException("a failed attempt must carry a failureReason");
        }
    }

    public static ProcessingAttempt success(ProcessingHop hop) {
        return new ProcessingAttempt(hop, AttemptOutcome.SUCCESS, null, Instant.now());
    }

    public static ProcessingAttempt failure(ProcessingHop hop, FailureReason reason) {
        return new ProcessingAttempt(hop, AttemptOutcome.FAILURE, reason, Instant.now());
    }
}
