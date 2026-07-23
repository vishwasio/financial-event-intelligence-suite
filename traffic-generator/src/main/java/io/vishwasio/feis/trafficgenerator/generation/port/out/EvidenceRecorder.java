package io.vishwasio.feis.trafficgenerator.generation.port.out;

import io.vishwasio.feis.trafficgenerator.generation.domain.event.GenerationEvent;

/**
 * Outbound port for recording a {@link GenerationEvent} as evidence.
 *
 * <p>Implementations (Milestone 6) are responsible for fanning a
 * single event out to all evidence sinks — PostgreSQL, Kafka,
 * structured logs, and OpenTelemetry traces — so callers only need to
 * depend on this one interface rather than four.
 */
public interface EvidenceRecorder {

    void record(GenerationEvent event);
}
