package io.vishwasio.feis.trafficgenerator.generation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CustomerTest {

    @Test
    void of_createsCustomerWithGeneratedId() {
        Customer customer = Customer.of("Jordan Ellis");

        assertThat(customer.id()).isNotNull();
        assertThat(customer.displayName()).isEqualTo("Jordan Ellis");
    }

    @Test
    void constructor_rejectsBlankDisplayName() {
        CustomerId id = CustomerId.generate();

        assertThatThrownBy(() -> new Customer(id, " "))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
