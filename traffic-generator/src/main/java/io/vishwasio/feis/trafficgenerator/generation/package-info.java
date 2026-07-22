/**
 * Deterministic financial event generation.
 *
 * <p>Produces historical evidence — PostgreSQL records, Kafka events,
 * structured logs, and OpenTelemetry traces — consumed later by
 * Financial Event Intelligence in read-only mode. This feature never
 * communicates with Financial Event Intelligence directly.
 */
package io.vishwasio.feis.trafficgenerator.generation;
