package io.vishwasio.feis.financialeventintelligence.investigation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InvestigationTest {

    private Subject sampleSubject() {
        return Subject.of(SubjectType.TRANSACTION, "TXN-12345");
    }

    @Test
    void initiate_startsInReceivedStatus() {
        Investigation investigation = Investigation.initiate(sampleSubject());

        assertThat(investigation.status()).isEqualTo(InvestigationStatus.RECEIVED);
        assertThat(investigation.id()).isNotNull();
        assertThat(investigation.createdAt()).isNotNull();
    }

    @Test
    void markInProgress_fromReceived_succeeds() {
        Investigation investigation = Investigation.initiate(sampleSubject());

        investigation.markInProgress();

        assertThat(investigation.status()).isEqualTo(InvestigationStatus.IN_PROGRESS);
    }

    @Test
    void markInProgress_whenNotReceived_throws() {
        Investigation investigation = Investigation.initiate(sampleSubject());
        investigation.markInProgress();

        assertThatThrownBy(investigation::markInProgress)
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void complete_fromInProgress_succeeds() {
        Investigation investigation = Investigation.initiate(sampleSubject());
        investigation.markInProgress();

        investigation.complete();

        assertThat(investigation.status()).isEqualTo(InvestigationStatus.COMPLETED);
    }

    @Test
    void complete_whenReceived_throws() {
        Investigation investigation = Investigation.initiate(sampleSubject());

        assertThatThrownBy(investigation::complete)
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void fail_afterCompleted_throws() {
        Investigation investigation = Investigation.initiate(sampleSubject());
        investigation.markInProgress();
        investigation.complete();

        assertThatThrownBy(investigation::fail)
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void fail_fromInProgress_succeeds() {
        Investigation investigation = Investigation.initiate(sampleSubject());
        investigation.markInProgress();

        investigation.fail();

        assertThat(investigation.status()).isEqualTo(InvestigationStatus.FAILED);
    }

    @Test
    void equality_isBasedOnId() {
        Subject subject = sampleSubject();
        Investigation investigation = Investigation.initiate(subject);
        Investigation reconstituted = Investigation.reconstitute(
            investigation.id(), subject, investigation.createdAt(), investigation.status());

        assertThat(investigation).isEqualTo(reconstituted);
    }
}
