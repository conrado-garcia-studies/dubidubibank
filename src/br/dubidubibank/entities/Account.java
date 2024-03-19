package br.dubidubibank.entities;

import br.dubidubibank.enums.AccountTypeCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Account extends Entity {
    private Integer agencyNumber;
    private Double balance;
    private List<Limit> limits = new ArrayList<>();
    private Integer number;
    private String password;
    private AccountType type;
    private User user;

    public Account() {
    }

    public Account(Integer agencyNumber, Double balance, Integer number, String password, AccountType type, User user) {
        this.agencyNumber = agencyNumber;
        this.balance = balance;
        this.number = number;
        this.password = password;
        this.type = type;
        this.user = user;
    }

    public Optional<Integer> getAgencyNumber() {
        return Optional.ofNullable(agencyNumber);
    }

    public void setAgencyNumber(Integer agencyNumber) {
        this.agencyNumber = agencyNumber;
    }

    public Optional<Double> getBalance() {
        return Optional.ofNullable(balance);
    }

    private void setBalance(Double balance) {
        this.balance = balance;
    }

    public void deposit(double value) {
        if (balance == null) {
            throw new IllegalArgumentException("You cannot deposit in an account that doesn't have a balance.");
        }
        balance += value;
    }

    public void withdraw(double value) {
        if (balance == null) {
            throw new IllegalArgumentException("You cannot withdraw from an account that doesn't have a balance.");
        }
        balance -= value;
    }

    public List<Limit> getLimits() {
        return limits;
    }

    public Optional<Limit> getActiveLimit(Command command) {
        return limits //
                .stream() //
                .filter(limit -> limit.getCommand().equals(command)) //
                .filter(Limit::isActive) //
                .findFirst();
    }

    private void setLimits(List<Limit> limits) {
        this.limits = limits;
    }

    public void addLimit(Limit limit) {
        Optional<Limit> overlappingLimit = limit.getOverlappingLimit(limits);
        if (overlappingLimit.isPresent()) {
            throw new IllegalArgumentException(String.format("The limit you are trying to add overlaps this existing" //
                    + " limit: %s.", overlappingLimit.get()));
        }
        limits.add(limit);
    }

    public void removeLimit(Limit limit) {
        limits.remove(limit);
    }

    public Optional<Integer> getNumber() {
        return Optional.ofNullable(number);
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Optional<String> getPassword() {
        return Optional.ofNullable(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public Optional<User> getUser() {
        return Optional.ofNullable(user);
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(agencyNumber, account.agencyNumber) && Objects.equals(number, account.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agencyNumber, number);
    }

    @Override
    public String toString() {
        if (type.getCode() == AccountTypeCode.ANONYMOUS) {
            return String.format("User: %s", type);
        }
        return String.format("User: %s%nAgency: %s%nAccount: %s%nType: %s%nBalance: %.2f", user, agencyNumber, //
                number, type, balance);
    }
}
