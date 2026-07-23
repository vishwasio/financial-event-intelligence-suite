package io.vishwasio.feis.trafficgenerator.generation.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Collections;

/**
 * Aggregate root for a single simulated payment moving through the
 * pipeline: API Gateway -> Payment Processor -> Payment Gateway ->
 * Bank Adapter -> Settlement.
 *
 * <p>Retries resume from whichever hop failed, not from the start of
 * the pipeline. Each hop allows up to {@value #MAX_RETRIES_PER_HOP}
 * retries (i.e. up to {@value #MAX_ATTEMPTS_PER_HOP} total attempts)
 * before the payment request is dead-lettered.
 */
public final class PaymentRequest {

    private static final List<ProcessingHop> PIPELINE = List.of(
        ProcessingHop.API_GATEWAY,
        ProcessingHop.PAYMENT_PROCESSOR,
        ProcessingHop.PAYMENT_GATEWAY,
        ProcessingHop.BANK_ADAPTER
    );

    public static final int MAX_RETRIES_PER_HOP = 3;
    public static final int MAX_ATTEMPTS_PER_HOP = MAX_RETRIES_PER_HOP + 1;

    private final PaymentRequestId id;
    private final CustomerId customerId;
    private final BankAccountId bankAccountId;
    private final PaymentAmount amount;
    private final Instant createdAt;
    private final List<ProcessingAttempt> attempts;

    private PaymentRequestStatus status;
    private int currentHopIndex;
    private Instant settledAt;

    private PaymentRequest(PaymentRequestId id, CustomerId customerId, BankAccountId bankAccountId,
                           PaymentAmount amount, Instant createdAt, List<ProcessingAttempt> attempts,
                           PaymentRequestStatus status, int currentHopIndex, Instant settledAt) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.customerId = Objects.requireNonNull(customerId, "customerId must not be null");
        this.bankAccountId = Objects.requireNonNull(bankAccountId, "bankAccountId must not be null");
        this.amount = Objects.requireNonNull(amount, "amount must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
        this.attempts = new ArrayList<>(Objects.requireNonNull(attempts, "attempts must not be null"));
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.currentHopIndex = currentHopIndex;
        this.settledAt = settledAt;
    }

    public static PaymentRequest create(CustomerId customerId, BankAccountId bankAccountId,
                                        PaymentAmount amount) {
        return new PaymentRequest(PaymentRequestId.generate(), customerId, bankAccountId, amount,
            Instant.now(), List.of(), PaymentRequestStatus.CREATED, 0, null);
    }

    /**
     * Reconstitutes a payment request from persisted state. Intended
     * for adapter-layer use, not for creating new payment requests.
     */
    public static PaymentRequest reconstitute(PaymentRequestId id, CustomerId customerId,
                                              BankAccountId bankAccountId, PaymentAmount amount,
                                              Instant createdAt, List<ProcessingAttempt> attempts,
                                              PaymentRequestStatus status, int currentHopIndex,
                                              Instant settledAt) {
        return new PaymentRequest(id, customerId, bankAccountId, amount, createdAt, attempts,
            status, currentHopIndex, settledAt);
    }

    /** Begins processing, moving from {@code CREATED} to the first pipeline hop. */
    public void startProcessing() {
        requireStatus(PaymentRequestStatus.CREATED, "start processing");
        this.status = PaymentRequestStatus.PROCESSING;
    }

    /** The hop the next {@link ProcessingAttempt} must be recorded against. */
    public ProcessingHop currentHop() {
        requireStatus(PaymentRequestStatus.PROCESSING, "determine current hop");
        return PIPELINE.get(currentHopIndex);
    }

    /**
     * Records the outcome of an attempt at the current hop.
     *
     * <p>On success: advances to the next hop, or settles if this was
     * the last hop. On failure: if retries remain at this hop, the
     * payment request stays at the same hop awaiting a retry;
     * otherwise it is dead-lettered.
     */
    public void recordAttempt(ProcessingAttempt attempt) {
        requireStatus(PaymentRequestStatus.PROCESSING, "record a processing attempt");
        ProcessingHop expectedHop = currentHop();
        if (attempt.hop() != expectedHop) {
            throw new IllegalArgumentException(
                "attempt hop %s does not match current hop %s".formatted(attempt.hop(), expectedHop));
        }

        attempts.add(attempt);

        if (attempt.outcome() == AttemptOutcome.SUCCESS) {
            if (currentHopIndex == PIPELINE.size() - 1) {
                settle();
            } else {
                currentHopIndex++;
            }
            return;
        }

        if (attemptsAtHop(expectedHop) >= MAX_ATTEMPTS_PER_HOP) {
            this.status = PaymentRequestStatus.DEAD_LETTERED;
        }
        // otherwise: remain PROCESSING at the same hop, awaiting a retry
    }

    private void settle() {
        this.status = PaymentRequestStatus.SETTLED;
        this.settledAt = Instant.now();
    }

    /** Explicitly marks this payment request as failed outside the retry/DLQ path. */
    public void fail() {
        if (this.status == PaymentRequestStatus.SETTLED) {
            throw new IllegalStateException("cannot fail a settled payment request");
        }
        this.status = PaymentRequestStatus.FAILED;
    }

    private long attemptsAtHop(ProcessingHop hop) {
        return attempts.stream().filter(a -> a.hop() == hop).count();
    }

    private void requireStatus(PaymentRequestStatus required, String action) {
        if (this.status != required) {
            throw new IllegalStateException(
                "cannot %s: payment request %s is in status %s, expected %s"
                    .formatted(action, id, status, required));
        }
    }

    public PaymentRequestId id() {
        return id;
    }

    public CustomerId customerId() {
        return customerId;
    }

    public BankAccountId bankAccountId() {
        return bankAccountId;
    }

    public PaymentAmount amount() {
        return amount;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public List<ProcessingAttempt> attempts() {
        return Collections.unmodifiableList(attempts);
    }

    public PaymentRequestStatus status() {
        return status;
    }

    public Instant settledAt() {
        return settledAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentRequest that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "PaymentRequest{id=%s, status=%s, currentHopIndex=%d, attempts=%d}"
            .formatted(id, status, currentHopIndex, attempts.size());
    }
}
