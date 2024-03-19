package br.dubidubibank.daos.impl;

import br.dubidubibank.daos.AccountDao;
import br.dubidubibank.entities.Account;
import br.dubidubibank.enums.AccountTypeCode;
import br.dubidubibank.persistence.InMemoryDatabase;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class InMemoryAccountDao implements AccountDao {
    private final InMemoryDatabase database;

    public InMemoryAccountDao(InMemoryDatabase database) {
        this.database = database;
    }

    @Override
    public Optional<Account> get(String id) {
        return database.getAccounts() //
                .stream() //
                .filter(account -> account.getId().map(id::equals).orElse(false)) //
                .findFirst();
    }

    @Override
    public Optional<Account> get(int agencyNumber, int number) {
        return database.getAccounts() //
                .stream() //
                .filter(account -> account.getAgencyNumber().map(Integer.valueOf(agencyNumber)::equals).orElse(false) //
                        && account.getNumber().map(Integer.valueOf(number)::equals).orElse(false)) //
                .findFirst();
    }

    @Override
    public Optional<Account> get(int agencyNumber, int number, String password) {
        return database.getAccounts() //
                .stream() //
                .filter(account -> account.getAgencyNumber().map(Integer.valueOf(agencyNumber)::equals).orElse(false) //
                        && account.getNumber().map(Integer.valueOf(number)::equals).orElse(false) //
                        && account.getPassword().map(password::equals).orElse(false)) //
                .findFirst();
    }

    @Override
    public Collection<Account> getAll() {
        return database.getAccounts();
    }

    @Override
    public Account getAnonymous() {
        return database.getAccounts() //
                .stream() //
                .filter(account -> account.getType().getCode() == AccountTypeCode.ANONYMOUS) //
                .findFirst() //
                .orElseThrow(() -> new NoSuchElementException("An anonymous account must exist in the database for" //
                        + " the application to work. Please contact the administrator."));
    }

    @Override
    public void remove(Account account) {
        database.getAccounts().remove(account);
    }

    @Override
    public void save(Account account) {
        if (account.getId().isEmpty()) {
            account.setId(UUID.randomUUID().toString());
        }
        database.getAccounts().add(account);
    }
}
