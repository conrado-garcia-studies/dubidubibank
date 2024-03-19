package br.dubidubibank.services.impl;

import br.dubidubibank.daos.AccountDao;
import br.dubidubibank.entities.Account;
import br.dubidubibank.services.SessionService;
import br.dubidubibank.session.Session;

import java.util.Optional;

public class DefaultSessionService implements SessionService {
    private final AccountDao accountDao;
    private Session session;

    public DefaultSessionService(AccountDao accountDao) {
        this.accountDao = accountDao;
        logOut();
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public void logIn(int agencyNumber, int accountNumber, String password) {
        Optional<Account> account = accountDao.get(agencyNumber, accountNumber, password);
        if (account.isPresent()) {
            session = new Session(account.get());
        } else {
            logOut();
        }
    }

    @Override
    public void logOut() {
        Account anonymousAccount = accountDao.getAnonymous();
        session = new Session(anonymousAccount);
    }
}
