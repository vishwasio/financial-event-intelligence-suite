package io.vishwasio.feis.financialeventintelligence.investigation;

import java.util.Objects;

/**
 * The entity an {@link Investigation} is being conducted about.
 *
 * <p>{@code reference} is the identifier of this subject within the
 * historical data produced by the traffic generator (e.g. a
 * transaction ID or gateway name) — the investigation engine resolves
 * it into evidence during the Resolve Subject / Collect Evidence
 * stages.
 */
public record Subject(SubjectId id, SubjectType type, String reference) {

    public Subject {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(reference, "reference must not be null");
        if (reference.isBlank()) {
            throw new IllegalArgumentException("reference must not be blank");
        }
    }

    public static Subject of(SubjectType type, String reference) {
        return new Subject(SubjectId.generate(), type, reference);
    }
}
