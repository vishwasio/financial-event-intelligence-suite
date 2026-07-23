package io.vishwasio.feis.trafficgenerator.generation;

/**
 * Coarse lifecycle status of a {@link PaymentRequest}.
 *
 * <p>Deliberately does not mirror each pipeline hop — hop-by-hop
 * detail lives in the {@link ProcessingAttempt} history, not in this
 * status, so this state machine stays stable even if the pipeline
 * topology changes.
 */
public enum PaymentRequestStatus {
    CREATED,
    PROCESSING,
    SETTLED,
    FAILED,
    DEAD_LETTERED
}
