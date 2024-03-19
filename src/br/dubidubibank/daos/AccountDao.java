package br.dubidubibank.daos;

import br.dubidubibank.entities.Account;

import java.util.Optional;

/**
 * Provides essential DAO functionalities for {@link Account}.
 */
public interface AccountDao extends Dao<Account> {
    /**
     * Gets an account by an agency number and an account number.
     *
     * @param agencyNumber The agency number
     * @param number       The account number
     * @return The account
     */
    Optional<Account> get(int agencyNumber, int number);

    /**
     * Gets an account by an agency number, an account number and a password.
     *
     * @param agencyNumber The agency number
     * @param number       The account number
     * @param password     The password
     * @return The account
     */
    Optional<Account> get(int agencyNumber, int number, String password);

    /**
     * Gets the anonymous account.
     *
     * @return The anonymous account
     */
    Account getAnonymous();
}
