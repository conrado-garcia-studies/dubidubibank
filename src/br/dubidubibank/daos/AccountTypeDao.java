package br.dubidubibank.daos;

import br.dubidubibank.entities.AccountType;

import java.util.Collection;

/**
 * Provides essential DAO functionalities for {@link AccountType}.
 */
public interface AccountTypeDao extends Dao<AccountType> {
    /**
     * Gets account types that the user can select.
     *
     * @return The account types
     */
    Collection<AccountType> getSelectable();
}
