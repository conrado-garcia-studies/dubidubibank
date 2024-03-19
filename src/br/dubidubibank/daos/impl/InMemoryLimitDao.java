package br.dubidubibank.daos.impl;

import br.dubidubibank.daos.LimitDao;
import br.dubidubibank.entities.Limit;
import br.dubidubibank.persistence.InMemoryDatabase;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class InMemoryLimitDao implements LimitDao {
    private final InMemoryDatabase database;

    public InMemoryLimitDao(InMemoryDatabase database) {
        this.database = database;
    }

    @Override
    public Optional<Limit> get(String id) {
        return database.getLimits() //
                .stream() //
                .filter(limit -> limit.getId().map(id::equals).orElse(false)) //
                .findFirst();
    }

    @Override
    public Collection<Limit> getAll() {
        return database.getLimits();
    }

    @Override
    public void remove(Limit limit) {
        database.getLimits().remove(limit);
    }

    @Override
    public void save(Limit limit) {
        if (limit.getId().isEmpty()) {
            limit.setId(UUID.randomUUID().toString());
        }
        database.getLimits().add(limit);
    }
}
