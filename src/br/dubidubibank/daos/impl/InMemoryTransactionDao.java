package br.dubidubibank.daos.impl;

import br.dubidubibank.daos.TransactionDao;
import br.dubidubibank.entities.Account;
import br.dubidubibank.entities.Command;
import br.dubidubibank.entities.Transaction;
import br.dubidubibank.persistence.InMemoryDatabase;
import br.dubidubibank.utils.DateUtils;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTransactionDao implements TransactionDao {
    private final InMemoryDatabase database;

    public InMemoryTransactionDao(InMemoryDatabase database) {
        this.database = database;
    }

    @Override
    public List<Transaction> get(Account account) {
        return database.getTransactions() //
                .stream() //
                .filter(transaction -> transaction.getSourceAccount().equals(account) //
                        || transaction.getTargetAccount().equals(account)) //
                .sorted(Comparator.comparing(Transaction::getCreationTime).reversed()) //
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Transaction> get(String id) {
        return database.getTransactions() //
                .stream() //
                .filter(transaction -> transaction.getId().map(id::equals).orElse(false)) //
                .findFirst();
    }

    @Override
    public Collection<Transaction> getAll() {
        return database.getTransactions() //
                .stream() //
                .sorted(Comparator.comparing(Transaction::getCreationTime).reversed()) //
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Transaction> getTodays(Account account, Command command) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime start = now.with(LocalTime.MIN);
        ZonedDateTime end = start.plusDays(1L).minusNanos(1L);
        return database.getTransactions() //
                .stream() //
                .filter(transaction -> (transaction.getSourceAccount().equals(account) //
                        || transaction.getTargetAccount().equals(account)) //
                        && transaction.getCommand().equals(command) //
                        && DateUtils.isToday(transaction.getCreationTime())) //
                .findFirst();
    }

    @Override
    public void remove(Transaction transaction) {
        database.getTransactions().remove(transaction);
    }

    @Override
    public void save(Transaction transaction) {
        if (transaction.getId().isEmpty()) {
            transaction.setId(UUID.randomUUID().toString());
        }
        database.getTransactions().add(transaction);
    }
}
