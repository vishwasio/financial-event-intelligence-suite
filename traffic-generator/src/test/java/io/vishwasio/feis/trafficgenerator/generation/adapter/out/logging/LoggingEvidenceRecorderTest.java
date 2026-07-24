package io.vishwasio.feis.trafficgenerator.generation.adapter.out.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import io.vishwasio.feis.trafficgenerator.generation.domain.CustomerId;
import io.vishwasio.feis.trafficgenerator.generation.domain.PaymentRequestId;
import io.vishwasio.feis.trafficgenerator.generation.domain.ProcessingAttempt;
import io.vishwasio.feis.trafficgenerator.generation.domain.ProcessingHop;
import io.vishwasio.feis.trafficgenerator.generation.domain.event.CustomerCreated;
import io.vishwasio.feis.trafficgenerator.generation.domain.event.ProcessingAttemptRecorded;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class LoggingEvidenceRecorderTest {

    private ListAppender<ILoggingEvent> listAppender;
    private Logger evidenceLogger;
    private LoggingEvidenceRecorder recorder;

    @BeforeEach
    void setUp() {
        evidenceLogger = (Logger) LoggerFactory.getLogger("evidence");
        listAppender = new ListAppender<>();
        listAppender.start();
        evidenceLogger.addAppender(listAppender);
        recorder = new LoggingEvidenceRecorder();
    }

    @AfterEach
    void tearDown() {
        evidenceLogger.detachAppender(listAppender);
    }

    @Test
    void record_customerCreated_logsAtInfoLevel() {
        CustomerCreated event = new CustomerCreated(CustomerId.generate(), "Jordan Ellis", Instant.now());

        recorder.record(event);

        assertThat(listAppender.list).hasSize(1);
        assertThat(listAppender.list.get(0).getLevel()).isEqualTo(Level.INFO);
        assertThat(listAppender.list.get(0).getMessage()).isEqualTo("customer created");
    }

    @Test
    void record_processingAttemptRecorded_logsAtInfoLevel() {
        ProcessingAttemptRecorded event = new ProcessingAttemptRecorded(
            PaymentRequestId.generate(), ProcessingAttempt.success(ProcessingHop.API_GATEWAY));

        recorder.record(event);

        assertThat(listAppender.list).hasSize(1);
        assertThat(listAppender.list.get(0).getMessage()).isEqualTo("processing attempt recorded");
    }
}
