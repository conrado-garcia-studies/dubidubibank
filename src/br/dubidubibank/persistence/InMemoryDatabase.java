package br.dubidubibank.persistence;

import br.dubidubibank.entities.*;

import java.util.Set;

/**
 * Stores objects in memory to simulate a database.
 */
public interface InMemoryDatabase {
    /**
     * Gets account types.
     *
     * @return The account types
     */
    Set<AccountType> getAccountTypes();

    /**
     * Gets accounts.
     *
     * @return The accounts
     */
    Set<Account> getAccounts();

    /**
     * Gets commands.
     *
     * @return The commands
     */
    Set<Command> getCommands();

    /**
     * Gets limits.
     *
     * @return The limits
     */
    Set<Limit> getLimits();

    /**
     * Gets transactions.
     *
     * @return The transactions
     */
    Set<Transaction> getTransactions();

    /**
     * Gets users.
     *
     * @return The users
     */
    Set<User> getUsers();
}
