package io.vishwasio.feis.trafficgenerator.generation.adapter.out.logging;

import io.vishwasio.feis.trafficgenerator.generation.domain.event.BankAccountCreated;
import io.vishwasio.feis.trafficgenerator.generation.domain.event.CustomerCreated;
import io.vishwasio.feis.trafficgenerator.generation.domain.event.GenerationEvent;
import io.vishwasio.feis.trafficgenerator.generation.domain.event.PaymentRequestCreated;
import io.vishwasio.feis.trafficgenerator.generation.domain.event.ProcessingAttemptRecorded;
import io.vishwasio.feis.trafficgenerator.generation.port.out.EvidenceRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

/**
 * {@link EvidenceRecorder} that writes each {@link GenerationEvent} as a
 * structured JSON log entry to the dedicated "evidence" logger (routed
 * to {@code logs/evidence.log} by logback-spring.xml).
 */
@Component
public class LoggingEvidenceRecorder implements EvidenceRecorder {

    private static final Logger log = LoggerFactory.getLogger("evidence");

    @Override
    public void record(GenerationEvent event) {
        switch (event) {
            case CustomerCreated e -> log.info("customer created",
                kv("eventType", "CustomerCreated"),
                kv("customerId", e.customerId().value()),
                kv("displayName", e.displayName()),
                kv("occurredAt", e.occurredAt()));

            case BankAccountCreated e -> log.info("bank account created",
                kv("eventType", "BankAccountCreated"),
                kv("bankAccountId", e.bankAccountId().value()),
                kv("ownerId", e.ownerId().value()),
                kv("bankIdentifier", e.bankIdentifier()),
                kv("occurredAt", e.occurredAt()));

            case PaymentRequestCreated e -> log.info("payment request created",
                kv("eventType", "PaymentRequestCreated"),
                kv("paymentRequestId", e.paymentRequestId().value()),
                kv("customerId", e.customerId().value()),
                kv("bankAccountId", e.bankAccountId().value()),
                kv("amount", e.amount().value()),
                kv("currency", e.amount().currencyCode()),
                kv("occurredAt", e.occurredAt()));

            case ProcessingAttemptRecorded e -> log.info("processing attempt recorded",
                kv("eventType", "ProcessingAttemptRecorded"),
                kv("paymentRequestId", e.paymentRequestId().value()),
                kv("hop", e.attempt().hop()),
                kv("outcome", e.attempt().outcome()),
                kv("failureReason", e.attempt().failureReason()),
                kv("occurredAt", e.occurredAt()));
        }
    }
}
