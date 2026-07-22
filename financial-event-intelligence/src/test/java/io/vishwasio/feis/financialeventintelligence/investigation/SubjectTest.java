package io.vishwasio.feis.financialeventintelligence.investigation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubjectTest {

    @Test
    void of_createsSubjectWithGeneratedId() {
        Subject subject = Subject.of(SubjectType.TRANSACTION, "TXN-12345");

        assertThat(subject.id()).isNotNull();
        assertThat(subject.type()).isEqualTo(SubjectType.TRANSACTION);
        assertThat(subject.reference()).isEqualTo("TXN-12345");
    }

    @Test
    void constructor_rejectsBlankReference() {
        SubjectId id = SubjectId.generate();

        assertThatThrownBy(() -> new Subject(id, SubjectType.GATEWAY, " "))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
