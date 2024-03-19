package br.dubidubibank.persistence.impl;

import br.dubidubibank.entities.*;
import br.dubidubibank.enums.AccountTypeCode;
import br.dubidubibank.enums.CommandCode;
import br.dubidubibank.persistence.InMemoryDatabase;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DefaultInMemoryDatabase implements InMemoryDatabase {
    private final Set<AccountType> accountTypes = new HashSet<>();
    private final Set<Account> accounts = new HashSet<>();
    private final Set<Command> commands = new HashSet<>();
    private final Set<Limit> limits = new HashSet<>();
    private final Set<Transaction> transactions = new HashSet<>();
    private final Set<User> users = new HashSet<>();

    public DefaultInMemoryDatabase() {
        Command depositCommand = createCommand( //
                CommandCode.DEPOSIT, //
                "d", //
                "[D]eposit", //
                true, "Deposit", //
                false //
        );
        Command exportCommand = createCommand( //
                CommandCode.EXPORT, //
                "e", //
                "[E]xport", //
                false, "Export", //
                false //
        );
        Command logInCommand = createCommand( //
                CommandCode.LOG_IN, //
                "l", //
                "[L]og in", //
                false, "Log in", //
                false //
        );
        Command logOutCommand = createCommand( //
                CommandCode.LOG_OUT, //
                "l", //
                "[L]og out", //
                false, "Log out", //
                false //
        );
        Command openCommand = createCommand( //
                CommandCode.OPEN, //
                "o", //
                "[O]pen", //
                false, "Open", //
                false //
        );
        Command quitCommand = createCommand( //
                CommandCode.QUIT, //
                "q", //
                "[Q]uit", //
                false, "Quit", //
                true //
        );
        Command restrictCommand = createCommand( //
                CommandCode.RESTRICT, //
                "r", //
                "[R]estrict", //
                false, "Restrict", //
                false //
        );
        Command transferCommand = createCommand( //
                CommandCode.TRANSFER, //
                "t", //
                "[T]ransfer", //
                true, "Transfer", //
                false //
        );
        Command withdrawCommand = createCommand( //
                CommandCode.WITHDRAW, //
                "w", //
                "[W]ithdraw", //
                true, "Withdraw", //
                false //
        );
        Limit lunchTimeDepositLimit = createLimit( //
                depositCommand, //
                "13:30", //,
                "11:30", //
                123.45d //
        );
        Limit lunchTimeWithdrawLimit = createLimit( //
                withdrawCommand, //
                "13:30", //
                "11:30", //
                12.34d //
        );
        Limit lunchTimeTransferLimit = createLimit( //
                transferCommand, //
                "13:30", //
                "11:30", //
                1234.56d //
        );
        Limit partyTimeDepositLimit = createLimit( //
                depositCommand, //
                "23:55", //
                "22:45", //
                12.34d //
        );
        Limit partyTimeWithdrawLimit = createLimit( //
                withdrawCommand, //
                "23:55", //
                "22:45", //
                1.23d //
        );
        Limit partyTimeTransferLimit = createLimit( //
                transferCommand, //
                "23:55", //
                "22:45", //
                123.45d //
        );
        AccountType adminAccountType = createAccountType( //
                AccountTypeCode.ADMIN, //
                "a", //
                "[A]dmin", //
                "Admin", //
                List.of(withdrawCommand, depositCommand, transferCommand, restrictCommand, exportCommand, //
                        openCommand, logOutCommand, quitCommand) //
        );
        AccountType anonymousAccountType = createAccountType( //
                AccountTypeCode.ANONYMOUS, //
                null, //
                null, //
                "Anonymous", //
                List.of(logInCommand, quitCommand) //
        );
        AccountType checkingAccountType = createAccountType( //
                AccountTypeCode.CHECKING, //
                "c", //
                "[C]hecking", //
                "Checking", //
                List.of(withdrawCommand, depositCommand, transferCommand, restrictCommand, exportCommand, //
                        logOutCommand, quitCommand) //
        );
        AccountType savingsAccountType = createAccountType( //
                AccountTypeCode.SAVINGS, //
                "s", //
                "[S]avings", //
                "Savings", //
                List.of(withdrawCommand, depositCommand, restrictCommand, logOutCommand, quitCommand) //
        );
        User adminUser = createUser( //
                "christell.rodriguez@gmail.com", //
                "Christell" //
        );
        User checkingUser = createUser( //
                "charlie1984@hotmail.com", //
                "Charlie Schmidt" //
        );
        User savingsUser = createUser( //
                "tardar.official@gmail.com", //
                "Tardar Sauce" //
        );
        createAccount( //
                1, //
                3454.46d, //
                List.of(lunchTimeDepositLimit, partyTimeTransferLimit), //
                1, //
                "admin", //
                adminAccountType, //
                adminUser //
        );
        createAccount( //
                null, //
                null, //
                List.of(), //
                null, //
                null, //
                anonymousAccountType, //
                null //
        );
        createAccount( //
                8, //
                4234.27d, //
                List.of(lunchTimeTransferLimit, partyTimeWithdrawLimit), //
                4, //
                "checking", //
                checkingAccountType, //
                checkingUser //
        );
        createAccount( //
                4, //
                1054.32d, //
                List.of(lunchTimeWithdrawLimit, partyTimeDepositLimit), //
                4, //
                "savings", //
                savingsAccountType, //
                savingsUser //
        );
    }

    private Command createCommand(CommandCode code, String input, String inputDescription, boolean limitable, String name, boolean terminal) {
        Command command = new Command(code, input, inputDescription, limitable, name, terminal);
        command.setId(UUID.randomUUID().toString());
        commands.add(command);
        return command;
    }

    private Limit createLimit(Command command, String endTimestamp, String startTimestamp, double value) {
        Limit limit = new Limit(command, endTimestamp, startTimestamp, value);
        limit.setId(UUID.randomUUID().toString());
        limits.add(limit);
        return limit;
    }

    private AccountType createAccountType(AccountTypeCode code, String input, String inputDescription, String name, List<Command> commands) {
        AccountType type = new AccountType(code, input, inputDescription, name);
        commands.forEach(type::addCommand);
        type.setId(UUID.randomUUID().toString());
        accountTypes.add(type);
        return type;
    }

    private User createUser(String email, String name) {
        User user = new User(email, name);
        user.setId(UUID.randomUUID().toString());
        users.add(user);
        return user;
    }

    private void createAccount(Integer agencyNumber, Double balance, List<Limit> limits, Integer number, String password, AccountType type, User user) {
        Account account = new Account(agencyNumber, balance, number, password, type, user);
        limits.forEach(account::addLimit);
        account.setId(UUID.randomUUID().toString());
        accounts.add(account);
    }

    @Override
    public Set<AccountType> getAccountTypes() {
        return accountTypes;
    }

    @Override
    public Set<Account> getAccounts() {
        return accounts;
    }

    @Override
    public Set<Command> getCommands() {
        return commands;
    }

    @Override
    public Set<Limit> getLimits() {
        return limits;
    }

    @Override
    public Set<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public Set<User> getUsers() {
        return users;
    }
}
