package io.vishwasio.feis.trafficgenerator.generation.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentRequestTest {

    private PaymentRequest samplePaymentRequest() {
        return PaymentRequest.create(CustomerId.generate(), BankAccountId.generate(),
            PaymentAmount.of(new BigDecimal("100.00"), "USD"));
    }

    @Test
    void create_startsInCreatedStatus() {
        PaymentRequest request = samplePaymentRequest();

        assertThat(request.status()).isEqualTo(PaymentRequestStatus.CREATED);
        assertThat(request.attempts()).isEmpty();
    }

    @Test
    void startProcessing_movesToFirstHop() {
        PaymentRequest request = samplePaymentRequest();

        request.startProcessing();

        assertThat(request.status()).isEqualTo(PaymentRequestStatus.PROCESSING);
        assertThat(request.currentHop()).isEqualTo(ProcessingHop.API_GATEWAY);
    }

    @Test
    void recordAttempt_successAtEachHop_settlesAfterLastHop() {
        PaymentRequest request = samplePaymentRequest();
        request.startProcessing();

        request.recordAttempt(ProcessingAttempt.success(ProcessingHop.API_GATEWAY));
        assertThat(request.currentHop()).isEqualTo(ProcessingHop.PAYMENT_PROCESSOR);

        request.recordAttempt(ProcessingAttempt.success(ProcessingHop.PAYMENT_PROCESSOR));
        assertThat(request.currentHop()).isEqualTo(ProcessingHop.PAYMENT_GATEWAY);

        request.recordAttempt(ProcessingAttempt.success(ProcessingHop.PAYMENT_GATEWAY));
        assertThat(request.currentHop()).isEqualTo(ProcessingHop.BANK_ADAPTER);

        request.recordAttempt(ProcessingAttempt.success(ProcessingHop.BANK_ADAPTER));

        assertThat(request.status()).isEqualTo(PaymentRequestStatus.SETTLED);
        assertThat(request.settledAt()).isNotNull();
    }

    @Test
    void recordAttempt_failureWithRetriesRemaining_staysAtSameHop() {
        PaymentRequest request = samplePaymentRequest();
        request.startProcessing();

        request.recordAttempt(ProcessingAttempt.failure(ProcessingHop.API_GATEWAY, FailureReason.GATEWAY_TIMEOUT));

        assertThat(request.status()).isEqualTo(PaymentRequestStatus.PROCESSING);
        assertThat(request.currentHop()).isEqualTo(ProcessingHop.API_GATEWAY);
        assertThat(request.attempts()).hasSize(1);
    }

    @Test
    void recordAttempt_retrySucceedsAfterFailure_resumesFromFailurePoint() {
        PaymentRequest request = samplePaymentRequest();
        request.startProcessing();

        request.recordAttempt(ProcessingAttempt.failure(ProcessingHop.API_GATEWAY, FailureReason.GATEWAY_TIMEOUT));
        request.recordAttempt(ProcessingAttempt.success(ProcessingHop.API_GATEWAY));

        assertThat(request.currentHop()).isEqualTo(ProcessingHop.PAYMENT_PROCESSOR);
        assertThat(request.attempts()).hasSize(2);
    }

    @Test
    void recordAttempt_fourthFailureAtSameHop_deadLetters() {
        PaymentRequest request = samplePaymentRequest();
        request.startProcessing();

        request.recordAttempt(ProcessingAttempt.failure(ProcessingHop.API_GATEWAY, FailureReason.GATEWAY_TIMEOUT));
        request.recordAttempt(ProcessingAttempt.failure(ProcessingHop.API_GATEWAY, FailureReason.GATEWAY_TIMEOUT));
        request.recordAttempt(ProcessingAttempt.failure(ProcessingHop.API_GATEWAY, FailureReason.GATEWAY_TIMEOUT));
        request.recordAttempt(ProcessingAttempt.failure(ProcessingHop.API_GATEWAY, FailureReason.GATEWAY_TIMEOUT));

        assertThat(request.status()).isEqualTo(PaymentRequestStatus.DEAD_LETTERED);
        assertThat(request.attempts()).hasSize(4);
    }

    @Test
    void recordAttempt_thirdFailure_stillAllowsOneMoreRetry() {
        PaymentRequest request = samplePaymentRequest();
        request.startProcessing();

        request.recordAttempt(ProcessingAttempt.failure(ProcessingHop.API_GATEWAY, FailureReason.GATEWAY_TIMEOUT));
        request.recordAttempt(ProcessingAttempt.failure(ProcessingHop.API_GATEWAY, FailureReason.GATEWAY_TIMEOUT));
        request.recordAttempt(ProcessingAttempt.failure(ProcessingHop.API_GATEWAY, FailureReason.GATEWAY_TIMEOUT));

        assertThat(request.status()).isEqualTo(PaymentRequestStatus.PROCESSING);
    }

    @Test
    void recordAttempt_wrongHop_throws() {
        PaymentRequest request = samplePaymentRequest();
        request.startProcessing();

        assertThatThrownBy(() ->
            request.recordAttempt(ProcessingAttempt.success(ProcessingHop.BANK_ADAPTER)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void recordAttempt_beforeStartProcessing_throws() {
        PaymentRequest request = samplePaymentRequest();

        assertThatThrownBy(() ->
            request.recordAttempt(ProcessingAttempt.success(ProcessingHop.API_GATEWAY)))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void fail_afterSettled_throws() {
        PaymentRequest request = samplePaymentRequest();
        request.startProcessing();
        for (ProcessingHop hop : ProcessingHop.values()) {
            request.recordAttempt(ProcessingAttempt.success(hop));
        }

        assertThatThrownBy(request::fail).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void attempts_isUnmodifiable() {
        PaymentRequest request = samplePaymentRequest();
        request.startProcessing();
        request.recordAttempt(ProcessingAttempt.success(ProcessingHop.API_GATEWAY));

        assertThatThrownBy(() -> request.attempts().add(ProcessingAttempt.success(ProcessingHop.API_GATEWAY)))
            .isInstanceOf(UnsupportedOperationException.class);
    }
}
