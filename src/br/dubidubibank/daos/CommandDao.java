package br.dubidubibank.daos;

import br.dubidubibank.entities.Command;
import br.dubidubibank.enums.CommandCode;

import java.util.Optional;

/**
 * Provides essential DAO functionalities for {@link Command}.
 */
public interface CommandDao extends Dao<Command> {
    /**
     * Gets a command by a code.
     *
     * @param code The code
     * @return The command
     */
    Optional<Command> get(CommandCode code);
}
