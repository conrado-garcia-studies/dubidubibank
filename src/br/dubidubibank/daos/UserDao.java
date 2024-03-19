package br.dubidubibank.daos;

import br.dubidubibank.entities.User;

import java.util.Optional;

/**
 * Provides essential DAO functionalities for {@link User}.
 */
public interface UserDao extends Dao<User> {
    /**
     * Gets a user by an email.
     *
     * @param email The email
     * @return The user
     */
    Optional<User> get2(String email);
}
