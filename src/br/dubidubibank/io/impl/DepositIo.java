package br.dubidubibank.io.impl;

import br.dubidubibank.daos.CommandDao;
import br.dubidubibank.daos.TransactionDao;
import br.dubidubibank.entities.Command;
import br.dubidubibank.entities.Limit;
import br.dubidubibank.entities.Transaction;
import br.dubidubibank.enums.CommandCode;
import br.dubidubibank.services.SessionService;
import br.dubidubibank.session.Session;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

public class DepositIo extends ScreenTemplateIo {
    private final CommandDao commandDao;
    private final TransactionDao transactionDao;

    public DepositIo(CommandDao commandDao, SessionService sessionService, TransactionDao transactionDao) {
        super(sessionService);
        this.commandDao = commandDao;
        this.transactionDao = transactionDao;
    }

    @Override
    protected void executeInternal(Scanner scanner) {
        System.out.println("How much do you want to deposit?");
        double value = scanner.nextDouble();
        if (value < 0) {
            throw new IllegalArgumentException("You cannot deposit a negative value.");
        }
        Optional<Command> command = commandDao.get(CommandCode.DEPOSIT);
        if (command.isEmpty()) {
            throw new NoSuchElementException("Could not find command.");
        }
        Session session = sessionService.getSession();
        Optional<Transaction> todaysTransaction = transactionDao.getTodays(session.getAccount(), command.get());
        if (todaysTransaction.isPresent()) {
            throw new IllegalArgumentException("You cannot deposit more than once a day. Please try again tomorrow.");
        }
        Optional<Limit> activeLimit = session.getAccount().getActiveLimit(command.get());
        if (activeLimit.isPresent() && value > activeLimit.get().getValue()) {
            throw new IllegalArgumentException(String.format("The value you chose exceeds the current limit of $%.2f," //
                            + " which goes from %s to %s.", activeLimit.get().getValue(), //
                    activeLimit.get().getStartTimestamp(), activeLimit.get().getEndTimestamp()));
        }
        Transaction transaction = new Transaction(command.get(), session.getAccount(), session.getAccount(), value);
        transactionDao.save(transaction);
        session.getAccount().deposit(value);
        System.out.println("The deposit was successful.");
        scanner.nextLine();
    }
}
