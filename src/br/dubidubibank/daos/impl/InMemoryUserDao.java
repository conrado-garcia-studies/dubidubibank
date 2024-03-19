package br.dubidubibank.daos.impl;

import br.dubidubibank.daos.UserDao;
import br.dubidubibank.entities.User;
import br.dubidubibank.persistence.InMemoryDatabase;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class InMemoryUserDao implements UserDao {
    private final InMemoryDatabase database;

    public InMemoryUserDao(InMemoryDatabase database) {
        this.database = database;
    }

    @Override
    public Optional<User> get(String id) {
        return database.getUsers() //
                .stream() //
                .filter(user -> user.getId().map(id::equals).orElse(false)) //
                .findFirst();
    }

    @Override
    public Optional<User> get2(String email) {
        return database.getUsers() //
                .stream() //
                .filter(user -> user.getEmail().equals(email)) //
                .findFirst();
    }

    @Override
    public Collection<User> getAll() {
        return database.getUsers();
    }

    @Override
    public void remove(User user) {
        database.getUsers().remove(user);
    }

    @Override
    public void save(User user) {
        if (user.getId().isEmpty()) {
            user.setId(UUID.randomUUID().toString());
        }
        database.getUsers().add(user);
    }
}
