package io.vishwasio.feis.trafficgenerator.generation.port.in;

import java.util.Objects;
import java.util.Optional;

/**
 * Input to {@link GenerateTrafficUseCase#generate}, mirroring the
 * POST /generate request parameters from the spec.
 */
public record GenerationCommand(int transactionCount, double failureRate, Long randomSeed,
                                boolean clearExistingData) {

    public GenerationCommand {
        if (transactionCount <= 0) {
            throw new IllegalArgumentException("transactionCount must be positive");
        }
        if (failureRate < 0.0 || failureRate > 1.0) {
            throw new IllegalArgumentException("failureRate must be between 0.0 and 1.0");
        }
    }

    public Optional<Long> seed() {
        return Optional.ofNullable(randomSeed);
    }
}
