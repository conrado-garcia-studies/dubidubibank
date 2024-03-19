package br.dubidubibank.entities;

import java.io.Serializable;
import java.util.Optional;

public abstract class Entity implements Serializable {
    protected String id;

    public Entity() {
    }

    public Entity(String id) {
        this.id = id;
    }

    public Optional<String> getId() {
        return Optional.ofNullable(id);
    }

    public void setId(String id) {
        this.id = id;
    }
}
