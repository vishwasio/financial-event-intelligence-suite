# Financial Event Intelligence Suite

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

_by [Vishwas Karode](https://github.com/vishwasio)_

An AI-assisted engineering investigation framework for distributed
financial systems.

This monorepo contains two independently evolving applications:

- **traffic-generator** — produces deterministic, historical financial
  event evidence (PostgreSQL, Kafka, structured logs, OpenTelemetry
  traces). It does not investigate anything.
- **financial-event-intelligence** — consumes that evidence in
  read-only mode and investigates it: evidence collection, evidence
  graph construction, deterministic analysis, knowledge model
  synthesis, and AI-assisted explanation over an immutable
  investigation snapshot.

The two applications never communicate directly (no REST, no RPC, no
synchronous calls). They are decoupled entirely through the artifacts
the traffic generator writes.

## Engineering Philosophy

Domain first, framework second. Deterministic facts are computed by
the investigation engine; AI explains those facts but never invents
or changes them. Evidence and investigation snapshots are immutable.

## Technology Stack

- Java 21
- Spring Boot 4.1.0
- Maven
- Hexagonal Architecture, feature-first packages
- PostgreSQL, Kafka, OpenTelemetry, Spring AI (introduced per milestone)

## Status

🚧 Early bootstrap — architecture and domain model defined, no
production code yet. See project spec for full architecture.

## Modules

| Module | Purpose | Status |
|---|---|---|
| `traffic-generator` | Produces deterministic financial evidence | Not started |
| `financial-event-intelligence` | Investigates evidence, AI-assisted analysis | Not started |

## Local Development

Start local infrastructure (PostgreSQL, Kafka):

    docker compose up -d

Stop it:

    docker compose down

Stop and wipe all data:

    docker compose down -v
