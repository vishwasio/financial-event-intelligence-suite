package io.vishwasio.feis.trafficgenerator.generation.domain;

/**
 * A stage in the simulated payment processing pipeline, in the order
 * a {@link PaymentRequest} passes through them.
 */
public enum ProcessingHop {
    API_GATEWAY,
    PAYMENT_PROCESSOR,
    PAYMENT_GATEWAY,
    BANK_ADAPTER
}
