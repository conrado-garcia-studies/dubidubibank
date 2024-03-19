package br.dubidubibank.entities;

import java.time.ZonedDateTime;
import java.util.Objects;

public class Transaction extends Entity {
    private Command command;
    private ZonedDateTime creationTime;
    private Account sourceAccount;
    private Account targetAccount;
    private Double value;

    public Transaction() {
    }

    public Transaction(Command command, Account sourceAccount, Account targetAccount, Double value) {
        this.command = command;
        this.creationTime = ZonedDateTime.now();
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.value = value;
    }

    public Command getCommand() {
        return command;
    }

    private void setCommand(Command command) {
        this.command = command;
    }

    public ZonedDateTime getCreationTime() {
        return creationTime;
    }

    private void setCreationTime(ZonedDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    private void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public Account getTargetAccount() {
        return targetAccount;
    }

    private void setTargetAccount(Account targetAccount) {
        this.targetAccount = targetAccount;
    }

    public Double getValue() {
        return value;
    }

    private void setValue(Double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
