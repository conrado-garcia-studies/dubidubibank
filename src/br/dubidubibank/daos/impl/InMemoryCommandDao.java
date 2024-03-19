package br.dubidubibank.daos.impl;

import br.dubidubibank.daos.CommandDao;
import br.dubidubibank.entities.Command;
import br.dubidubibank.enums.CommandCode;
import br.dubidubibank.persistence.InMemoryDatabase;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class InMemoryCommandDao implements CommandDao {
    private final InMemoryDatabase database;

    public InMemoryCommandDao(InMemoryDatabase database) {
        this.database = database;
    }

    @Override
    public Optional<Command> get(CommandCode code) {
        return database.getCommands() //
                .stream() //
                .filter(command -> command.getCode() == code) //
                .findFirst();
    }

    @Override
    public Optional<Command> get(String id) {
        return database.getCommands() //
                .stream() //
                .filter(command -> command.getId().map(id::equals).orElse(false)) //
                .findFirst();
    }

    @Override
    public Collection<Command> getAll() {
        return database.getCommands();
    }

    @Override
    public void remove(Command command) {
        database.getCommands().remove(command);
    }

    @Override
    public void save(Command command) {
        if (command.getId().isEmpty()) {
            command.setId(UUID.randomUUID().toString());
        }
        database.getCommands().add(command);
    }
}
