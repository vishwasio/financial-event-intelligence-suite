package io.vishwasio.feis.financialeventintelligence.investigation;

import java.util.Objects;
import java.util.UUID;

/**
 * Identity of an {@link Investigation}.
 *
 * <p>Wrapping the raw {@link UUID} in a dedicated type prevents an
 * investigation's identity from being accidentally interchanged with
 * the identity of an unrelated aggregate (e.g. an evidence or subject
 * identifier) at compile time.
 */
public record InvestigationId(UUID value) {

    public InvestigationId {
        Objects.requireNonNull(value, "value must not be null");
    }

    public static InvestigationId generate() {
        return new InvestigationId(UUID.randomUUID());
    }

    public static InvestigationId of(UUID value) {
        return new InvestigationId(value);
    }
}
