package io.vishwasio.feis.trafficgenerator.generation.domain.event;

import java.time.Instant;

/**
 * Something that happened during traffic generation, destined to
 * become evidence (a Postgres record, a Kafka event, a log line, and
 * an OpenTelemetry span) via {@code EvidenceRecorder}.
 *
 * <p>Sealed so any adapter consuming these can pattern-match
 * exhaustively — adding a new event type is a compile error at every
 * consumer until handled.
 */
public sealed interface GenerationEvent
    permits CustomerCreated, BankAccountCreated, PaymentRequestCreated, ProcessingAttemptRecorded {

    Instant occurredAt();
}
