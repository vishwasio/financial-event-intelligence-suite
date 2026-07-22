package io.vishwasio.feis.financialeventintelligence.investigation;

/**
 * The category of entity a {@link Subject} refers to within the
 * historical financial data produced by the traffic generator.
 *
 * <p>Extensible by design — future investigation types should be
 * easy to add without touching existing pipeline logic.
 */
public enum SubjectType {
    TRANSACTION,
    GATEWAY,
    BANK,
    TRACE,
    CUSTOMER
}
