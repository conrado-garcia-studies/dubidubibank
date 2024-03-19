package br.dubidubibank.io.impl;

import br.dubidubibank.daos.AccountDao;
import br.dubidubibank.daos.CommandDao;
import br.dubidubibank.daos.TransactionDao;
import br.dubidubibank.entities.Account;
import br.dubidubibank.entities.Command;
import br.dubidubibank.entities.Limit;
import br.dubidubibank.entities.Transaction;
import br.dubidubibank.enums.CommandCode;
import br.dubidubibank.services.SessionService;
import br.dubidubibank.session.Session;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

public class TransferIo extends ScreenTemplateIo {
    private final AccountDao accountDao;
    private final CommandDao commandDao;
    private final TransactionDao transactionDao;

    public TransferIo(AccountDao accountDao, CommandDao commandDao, SessionService sessionService, TransactionDao transactionDao) {
        super(sessionService);
        this.accountDao = accountDao;
        this.commandDao = commandDao;
        this.transactionDao = transactionDao;
    }

    @Override
    protected void executeInternal(Scanner scanner) {
        System.out.println("Please type the information of the account you want to transfer to.");
        System.out.print("Agency: ");
        int agencyNumber = scanner.nextInt();
        System.out.print("Account: ");
        int accountNumber = scanner.nextInt();
        scanner.nextLine();
        Optional<Account> targetAccount = accountDao.get(agencyNumber, accountNumber);
        if (targetAccount.isEmpty()) {
            throw new IllegalArgumentException("The account was not found.");
        }
        Session session = sessionService.getSession();
        Account sourceAccount = session.getAccount();
        if (targetAccount.get().equals(sourceAccount)) {
            throw new IllegalArgumentException("You cannot transfer to yourself.");
        }
        System.out.print("How much do you want to transfer? ");
        double value = scanner.nextDouble();
        if (value < 0) {
            throw new IllegalArgumentException("You cannot transfer a negative value.");
        }
        Optional<Command> command = commandDao.get(CommandCode.TRANSFER);
        if (command.isEmpty()) {
            throw new NoSuchElementException("Could not find command.");
        }
        Optional<Transaction> todaysTransaction = transactionDao.getTodays(session.getAccount(), command.get());
        if (todaysTransaction.isPresent()) {
            throw new IllegalArgumentException("You cannot transfer more than once a day. Please try again tomorrow.");
        }
        Optional<Limit> activeLimit = session.getAccount().getActiveLimit(command.get());
        if (activeLimit.isPresent() && value > activeLimit.get().getValue()) {
            throw new IllegalArgumentException(String.format("The value you chose exceeds the current limit of $%.2f," //
                            + " which goes from %s to %s.", activeLimit.get().getValue(), //
                    activeLimit.get().getStartTimestamp(), activeLimit.get().getEndTimestamp()));
        }
        Transaction transaction = new Transaction(command.get(), sourceAccount, targetAccount.get(), value);
        transactionDao.save(transaction);
        sourceAccount.withdraw(value);
        targetAccount.get().deposit(value);
        System.out.println("The transfer was successful.");
        scanner.nextLine();
    }
}
