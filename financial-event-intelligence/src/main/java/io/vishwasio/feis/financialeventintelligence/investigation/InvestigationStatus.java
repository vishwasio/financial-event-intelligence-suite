package io.vishwasio.feis.financialeventintelligence.investigation;

/**
 * Lifecycle status of an {@link Investigation}, tracking its
 * macro-phase in the deterministic pipeline: Resolve Subject ->
 * Collect Evidence -> Build Evidence Graph -> Analyze Evidence ->
 * Generate Findings -> Compute Facts -> Generate Verdict -> Build
 * Knowledge Model -> Create Investigation Snapshot (all covered by
 * {@code IN_PROGRESS}) -> Generate AI Explanation -> Conversation
 * Ready (covered by {@code COMPLETED}).
 *
 * <p>This status intentionally does not enumerate every pipeline
 * step — fine-grained step tracking belongs in orchestration/logging,
 * not in the aggregate's state machine.
 */
public enum InvestigationStatus {

    /** Investigation created; subject resolved, pipeline not yet started. */
    RECEIVED,

    /** Deterministic pipeline (evidence through verdict) executing. */
    IN_PROGRESS,

    /** Snapshot persisted, AI explanation generated, conversation ready. */
    COMPLETED,

    /** Investigation could not be completed. */
    FAILED
}
