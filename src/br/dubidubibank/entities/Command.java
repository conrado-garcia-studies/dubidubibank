package br.dubidubibank.entities;

import br.dubidubibank.enums.CommandCode;

import java.util.Objects;

public class Command extends Entity {
    private CommandCode code;
    private String input;
    private String inputDescription;
    private Boolean limitable;
    private String name;
    private Boolean terminal;

    public Command() {
    }

    public Command(CommandCode code, String input, String inputDescription, Boolean limitable, String name, Boolean terminal) {
        this.code = code;
        this.input = input;
        this.inputDescription = inputDescription;
        this.limitable = limitable;
        this.name = name;
        this.terminal = terminal;
    }

    public CommandCode getCode() {
        return code;
    }

    public void setCode(CommandCode code) {
        this.code = code;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getInputDescription() {
        return inputDescription;
    }

    public void setInputDescription(String inputDescription) {
        this.inputDescription = inputDescription;
    }

    public Boolean getLimitable() {
        return limitable;
    }

    public void setLimitable(Boolean limitable) {
        this.limitable = limitable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getTerminal() {
        return terminal;
    }

    public void setTerminal(Boolean terminal) {
        this.terminal = terminal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return code == command.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return name;
    }
}
