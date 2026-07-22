package io.vishwasio.feis.trafficgenerator.generation;

import java.util.Objects;

/**
 * A simulated bank account belonging to a {@link Customer}.
 *
 * <p>{@code bankIdentifier} represents which simulated bank this
 * account belongs to (e.g. a generated bank code) — used later to
 * route a {@code PaymentRequest} to the correct simulated bank
 * adapter during the Bank Adapter hop.
 */
public record BankAccount(BankAccountId id, CustomerId ownerId, String bankIdentifier,
                          String accountNumber) {

    public BankAccount {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(ownerId, "ownerId must not be null");
        Objects.requireNonNull(bankIdentifier, "bankIdentifier must not be null");
        Objects.requireNonNull(accountNumber, "accountNumber must not be null");
        if (bankIdentifier.isBlank()) {
            throw new IllegalArgumentException("bankIdentifier must not be blank");
        }
        if (accountNumber.isBlank()) {
            throw new IllegalArgumentException("accountNumber must not be blank");
        }
    }

    public static BankAccount of(CustomerId ownerId, String bankIdentifier, String accountNumber) {
        return new BankAccount(BankAccountId.generate(), ownerId, bankIdentifier, accountNumber);
    }
}
