package io.vishwasio.feis.trafficgenerator.generation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BankAccountTest {

    @Test
    void of_createsBankAccountWithGeneratedId() {
        CustomerId ownerId = CustomerId.generate();

        BankAccount account = BankAccount.of(ownerId, "BANK-001", "ACC-98765");

        assertThat(account.id()).isNotNull();
        assertThat(account.ownerId()).isEqualTo(ownerId);
        assertThat(account.bankIdentifier()).isEqualTo("BANK-001");
        assertThat(account.accountNumber()).isEqualTo("ACC-98765");
    }

    @Test
    void constructor_rejectsBlankBankIdentifier() {
        CustomerId ownerId = CustomerId.generate();
        BankAccountId id = BankAccountId.generate();

        assertThatThrownBy(() -> new BankAccount(id, ownerId, " ", "ACC-98765"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor_rejectsBlankAccountNumber() {
        CustomerId ownerId = CustomerId.generate();
        BankAccountId id = BankAccountId.generate();

        assertThatThrownBy(() -> new BankAccount(id, ownerId, "BANK-001", " "))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
