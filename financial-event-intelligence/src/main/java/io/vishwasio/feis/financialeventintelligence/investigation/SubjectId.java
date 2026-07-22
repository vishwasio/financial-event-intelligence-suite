package io.vishwasio.feis.financialeventintelligence.investigation;

import java.util.Objects;
import java.util.UUID;

/**
 * Identity of a {@link Subject}.
 */
public record SubjectId(UUID value) {

    public SubjectId {
        Objects.requireNonNull(value, "value must not be null");
    }

    public static SubjectId generate() {
        return new SubjectId(UUID.randomUUID());
    }

    public static SubjectId of(UUID value) {
        return new SubjectId(value);
    }
}
