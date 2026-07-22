package io.vishwasio.feis.financialeventintelligence.investigation;

import java.time.Instant;
import java.util.Objects;

/**
 * Aggregate root for a single investigation into a {@link Subject}.
 *
 * <p>An {@code Investigation} owns its own lifecycle status but does
 * not compute analysis results itself — those are produced
 * deterministically by the {@code analysis} feature and synthesized
 * into a {@code KnowledgeModel}. This type only tracks identity,
 * subject, and pipeline status.
 *
 * <p>Status transitions are one-directional and validated: an
 * investigation cannot move backward in its lifecycle.
 */
public final class Investigation {

    private final InvestigationId id;
    private final Subject subject;
    private final Instant createdAt;
    private InvestigationStatus status;

    private Investigation(InvestigationId id, Subject subject, Instant createdAt,
                          InvestigationStatus status) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.subject = Objects.requireNonNull(subject, "subject must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
    }

    /**
     * Begins a new investigation for the given subject, in
     * {@link InvestigationStatus#RECEIVED} status.
     */
    public static Investigation initiate(Subject subject) {
        return new Investigation(InvestigationId.generate(), subject, Instant.now(),
            InvestigationStatus.RECEIVED);
    }

    /**
     * Reconstitutes an investigation from persisted state. Intended
     * for adapter-layer use (e.g. repository implementations), not
     * for starting new investigations.
     */
    public static Investigation reconstitute(InvestigationId id, Subject subject,
                                             Instant createdAt, InvestigationStatus status) {
        return new Investigation(id, subject, createdAt, status);
    }

    public void markInProgress() {
        requireStatus(InvestigationStatus.RECEIVED, "start pipeline processing");
        this.status = InvestigationStatus.IN_PROGRESS;
    }

    public void complete() {
        requireStatus(InvestigationStatus.IN_PROGRESS, "complete");
        this.status = InvestigationStatus.COMPLETED;
    }

    public void fail() {
        if (this.status == InvestigationStatus.COMPLETED) {
            throw new IllegalStateException("cannot fail a completed investigation");
        }
        this.status = InvestigationStatus.FAILED;
    }

    private void requireStatus(InvestigationStatus required, String action) {
        if (this.status != required) {
            throw new IllegalStateException(
                "cannot %s: investigation %s is in status %s, expected %s"
                    .formatted(action, id, status, required));
        }
    }

    public InvestigationId id() {
        return id;
    }

    public Subject subject() {
        return subject;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public InvestigationStatus status() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Investigation that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Investigation{id=%s, subject=%s, status=%s, createdAt=%s}"
            .formatted(id, subject, status, createdAt);
    }
}
