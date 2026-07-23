package io.vishwasio.feis.trafficgenerator.generation;

/**
 * The reason a single {@link ProcessingAttempt} failed.
 *
 * <p>Deliberately does not include retry exhaustion or dead-lettering
 * — those are outcomes of repeated failures at a hop, not causes of
 * an individual attempt's failure.
 */
public enum FailureReason {
    GATEWAY_TIMEOUT,
    BANK_TIMEOUT,
    BANK_REJECTION,
    VALIDATION_ERROR,
    NETWORK_LATENCY,
    DUPLICATE_MESSAGE
}
