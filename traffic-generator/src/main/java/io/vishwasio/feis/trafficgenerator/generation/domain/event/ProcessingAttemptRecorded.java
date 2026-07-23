package io.vishwasio.feis.trafficgenerator.generation.domain.event;

import io.vishwasio.feis.trafficgenerator.generation.domain.PaymentRequestId;
import io.vishwasio.feis.trafficgenerator.generation.domain.ProcessingAttempt;

import java.time.Instant;
import java.util.Objects;

public record ProcessingAttemptRecorded(PaymentRequestId paymentRequestId, ProcessingAttempt attempt)
    implements GenerationEvent {

    public ProcessingAttemptRecorded {
        Objects.requireNonNull(paymentRequestId, "paymentRequestId must not be null");
        Objects.requireNonNull(attempt, "attempt must not be null");
    }

    @Override
    public Instant occurredAt() {
        return attempt.occurredAt();
    }
}
