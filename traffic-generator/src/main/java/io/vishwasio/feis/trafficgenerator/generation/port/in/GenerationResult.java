package io.vishwasio.feis.trafficgenerator.generation.port.in;

import java.util.Objects;

/**
 * Outcome of a completed generation run.
 */
public record GenerationResult(int transactionsGenerated, int transactionsFailed,
                               int transactionsDeadLettered) {

    public GenerationResult {
        if (transactionsGenerated < 0 || transactionsFailed < 0 || transactionsDeadLettered < 0) {
            throw new IllegalArgumentException("counts must not be negative");
        }
    }
}
