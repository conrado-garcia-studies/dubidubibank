package br.dubidubibank.daos;

import java.util.Collection;
import java.util.Optional;

/**
 * Provides essential DAO functionalities for {@link T}.
 */
public interface Dao<T> {
    /**
     * Gets an item by an identifier.
     *
     * @param id The identifier
     * @return The item
     */
    Optional<T> get(String id);

    /**
     * Gets all items.
     *
     * @return The items
     */
    Collection<T> getAll();

    /**
     * Removes one item.
     *
     * @param t The item
     */
    void remove(T t);

    /**
     * Saves one item.
     *
     * @param t The item
     */
    void save(T t);
}
