package br.dubidubibank.entities;

import br.dubidubibank.enums.AccountTypeCode;

import java.util.*;
import java.util.stream.Collectors;

public class AccountType extends Entity {
    private AccountTypeCode code;
    private List<Command> commands = new ArrayList<>();
    private String input;
    private String inputDescription;
    private String name;

    public AccountType() {
    }

    public AccountType(AccountTypeCode code, String input, String inputDescription, String name) {
        this.code = code;
        this.input = input;
        this.inputDescription = inputDescription;
        this.name = name;
    }

    public AccountTypeCode getCode() {
        return code;
    }

    public void setCode(AccountTypeCode code) {
        this.code = code;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public List<Command> getLimitableCommands() {
        return commands //
                .stream() //
                .filter(Command::getLimitable) //
                .sorted(Comparator.comparing(Command::getInputDescription)) //
                .collect(Collectors.toList());
    }

    private void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    public void addCommand(Command command) {
        this.commands.add(command);
    }

    public void removeCommand(Command command) {
        this.commands.remove(command);
    }

    public Optional<String> getInput() {
        return Optional.ofNullable(input);
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Optional<String> getInputDescription() {
        return Optional.ofNullable(inputDescription);
    }

    public void setInputDescription(String inputDescription) {
        this.inputDescription = inputDescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountType that = (AccountType) o;
        return code == that.code;
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
