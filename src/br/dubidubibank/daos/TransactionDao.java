package br.dubidubibank.daos;

import br.dubidubibank.entities.Account;
import br.dubidubibank.entities.Command;
import br.dubidubibank.entities.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * Provides essential DAO functionalities for {@link Transaction}.
 */
public interface TransactionDao extends Dao<Transaction> {
    /**
     * Gets transactions by an account.
     *
     * @param account The account
     * @return The transactions
     */
    List<Transaction> get(Account account);

    /**
     * Gets today's transaction by an account and a command.
     *
     * @param account The account
     * @param command The command
     * @return The transaction
     */
    Optional<Transaction> getTodays(Account account, Command command);
}
