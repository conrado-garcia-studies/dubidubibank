package br.dubidubibank.services;

import br.dubidubibank.session.Session;

/**
 * Manages the session and provides information about the session.
 */
public interface SessionService {
    /**
     * Gets the current session.
     *
     * @return The session
     */
    Session getSession();

    /**
     * Logs in to an account by using an agency number, an account number and a password.
     *
     * @param agencyNumber  The agency number
     * @param accountNumber The account number
     * @param password      The password
     */
    void logIn(int agencyNumber, int accountNumber, String password);

    /**
     * Logs out from an account.
     */
    void logOut();
}
