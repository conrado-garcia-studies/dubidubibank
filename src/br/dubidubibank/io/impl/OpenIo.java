package br.dubidubibank.io.impl;

import br.dubidubibank.daos.AccountDao;
import br.dubidubibank.daos.AccountTypeDao;
import br.dubidubibank.daos.LimitDao;
import br.dubidubibank.daos.UserDao;
import br.dubidubibank.entities.*;
import br.dubidubibank.services.SessionService;
import br.dubidubibank.utils.IoUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class OpenIo extends ScreenTemplateIo {
    private final AccountDao accountDao;
    private final AccountTypeDao accountTypeDao;
    private final LimitDao limitDao;
    private final UserDao userDao;

    public OpenIo(AccountDao accountDao, AccountTypeDao accountTypeDao, LimitDao limitDao, SessionService sessionService, UserDao userDao) {
        super(sessionService);
        this.accountDao = accountDao;
        this.accountTypeDao = accountTypeDao;
        this.limitDao = limitDao;
        this.userDao = userDao;
    }

    @Override
    protected void executeInternal(Scanner scanner) {
        System.out.println("Please type the information of new account you want to open.");
        System.out.print("Agency: ");
        int agencyNumber = scanner.nextInt();
        System.out.print("Account: ");
        int accountNumber = scanner.nextInt();
        scanner.nextLine();
        Optional<Account> existingAccount = accountDao.get(agencyNumber, accountNumber);
        if (existingAccount.isPresent()) {
            throw new IllegalArgumentException("There is already an account with that number for that agency.");
        }
        Collection<AccountType> accountTypes = accountTypeDao.getSelectable();
        String inputDescriptions = IoUtils.getAccountTypeInputDescriptions(accountTypes);
        System.out.printf("What is the account type? (%s)%n", inputDescriptions);
        String input = scanner.nextLine();
        AccountType accountType = IoUtils.getAccountType(accountTypes, input);
        User user = readUser(scanner);
        System.out.print("Password: ");
        String password = scanner.nextLine();
        Account account = new Account(agencyNumber, 0d, accountNumber, password, accountType, user);
        List<Command> limitableCommands = account.getType().getLimitableCommands();
        if (!limitableCommands.isEmpty()) {
            createLimits(scanner, account, limitableCommands);
        }
        accountDao.save(account);
        System.out.println("The account was created successfully!");
    }

    private User readUser(Scanner scanner) {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        Optional<User> existingUser = userDao.get2(email);
        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            System.out.print("Name: ");
            String name = scanner.nextLine();
            user = new User(email, name);
            userDao.save(user);
        }
        return user;
    }

    private void createLimits(Scanner scanner, Account account, List<Command> commands) {
        boolean keepAsking = true;
        while (keepAsking) {
            try {
                if (account.getLimits().isEmpty()) {
                    System.out.println("Do you want to create a limit? ([Y]es, [N]o)");
                } else {
                    System.out.println("Do you want to create a new limit? ([Y]es, [N]o)");
                }
                String input = scanner.nextLine();
                if (IoUtils.isActualInputMatchesExpectedInput("n", input)) {
                    keepAsking = false;
                } else if (IoUtils.isActualInputMatchesExpectedInput("y", input)) {
                    String inputDescriptions = IoUtils.getCommandInputDescriptions(commands);
                    System.out.printf("Which command do you want to limit? (%s)%n", inputDescriptions);
                    input = scanner.nextLine();
                    Command command = IoUtils.getCommand(commands, input);
                    System.out.print("Start time (please type in the format HH:mm, e.g. 09:32, 23:37, etc.): ");
                    String startTimestamp = scanner.nextLine();
                    System.out.print("End time (please type a value at least 1 minute greater than the start time in" //
                            + " the format HH:mm, e.g. 09:32,  23:37, etc.): ");
                    String endTimestamp = scanner.nextLine();
                    System.out.print("Value: ");
                    Double value = scanner.nextDouble();
                    Limit limit = new Limit(command, endTimestamp, startTimestamp, value);
                    limitDao.save(limit);
                    account.addLimit(limit);
                    System.out.println("The limit was created successfully.");
                    scanner.nextLine();
                } else {
                    throw new IllegalArgumentException("You didn't type any valid option.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                scanner.nextLine();
            }
        }
    }
}
