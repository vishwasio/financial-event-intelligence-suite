package io.vishwasio.feis.trafficgenerator.generation.domain.event;

import io.vishwasio.feis.trafficgenerator.generation.domain.BankAccountId;
import io.vishwasio.feis.trafficgenerator.generation.domain.CustomerId;
import io.vishwasio.feis.trafficgenerator.generation.domain.PaymentAmount;
import io.vishwasio.feis.trafficgenerator.generation.domain.PaymentRequestId;

import java.time.Instant;
import java.util.Objects;

public record PaymentRequestCreated(PaymentRequestId paymentRequestId, CustomerId customerId,
                                    BankAccountId bankAccountId, PaymentAmount amount,
                                    Instant occurredAt) implements GenerationEvent {

    public PaymentRequestCreated {
        Objects.requireNonNull(paymentRequestId, "paymentRequestId must not be null");
        Objects.requireNonNull(customerId, "customerId must not be null");
        Objects.requireNonNull(bankAccountId, "bankAccountId must not be null");
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(occurredAt, "occurredAt must not be null");
    }
}
