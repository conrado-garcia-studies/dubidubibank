package br.dubidubibank.services;

import br.dubidubibank.entities.Account;

/**
 * Generates a transaction report and saves it to a folder.
 */
public interface TransactionExportService {
    /**
     * Generates a transaction report of an account and saves it to a folder.
     *
     * @param account The account
     * @return The name of the file of the report that was generated
     */
    String export(Account account);
}
