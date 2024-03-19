package br.dubidubibank.daos.impl;

import br.dubidubibank.daos.AccountTypeDao;
import br.dubidubibank.entities.AccountType;
import br.dubidubibank.persistence.InMemoryDatabase;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class InMemoryAccountTypeDao implements AccountTypeDao {
    private final InMemoryDatabase database;

    public InMemoryAccountTypeDao(InMemoryDatabase database) {
        this.database = database;
    }

    @Override
    public Optional<AccountType> get(String id) {
        return database.getAccountTypes() //
                .stream() //
                .filter(accountType -> accountType.getId().map(id::equals).orElse(false)) //
                .findFirst();
    }

    @Override
    public Collection<AccountType> getSelectable() {
        return database.getAccountTypes() //
                .stream() //
                .filter(accountType -> accountType.getInput().isPresent() && accountType.getInputDescription().isPresent()) //
                .collect(Collectors.toList());
    }

    @Override
    public Collection<AccountType> getAll() {
        return database.getAccountTypes();
    }

    @Override
    public void remove(AccountType accountType) {
        database.getAccountTypes().remove(accountType);
    }

    @Override
    public void save(AccountType accountType) {
        if (accountType.getId().isEmpty()) {
            accountType.setId(UUID.randomUUID().toString());
        }
        database.getAccountTypes().add(accountType);
    }
}
