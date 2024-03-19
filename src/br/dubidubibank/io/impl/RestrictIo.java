package br.dubidubibank.io.impl;

import br.dubidubibank.daos.AccountDao;
import br.dubidubibank.daos.LimitDao;
import br.dubidubibank.entities.Command;
import br.dubidubibank.entities.Limit;
import br.dubidubibank.services.SessionService;
import br.dubidubibank.session.Session;
import br.dubidubibank.utils.IoUtils;

import java.util.List;
import java.util.Scanner;

public class RestrictIo extends ScreenTemplateIo {
    private final AccountDao accountDao;
    private final LimitDao limitDao;

    public RestrictIo(AccountDao accountDao, LimitDao limitDao, SessionService sessionService) {
        super(sessionService);
        this.accountDao = accountDao;
        this.limitDao = limitDao;
    }

    @Override
    protected void executeInternal(Scanner scanner) {
        boolean keepAsking = true;
        while (keepAsking) {
            try {
                System.out.println("What do you want do do? ([C]reate limit, [E]dit limit, [R]emove limit, [Q]uit)");
                String input = scanner.nextLine();
                if (IoUtils.isActualInputMatchesExpectedInput("c", input)) {
                    createLimit(scanner);
                } else if (IoUtils.isActualInputMatchesExpectedInput("e", input)) {
                    editLimit(scanner);
                } else if (IoUtils.isActualInputMatchesExpectedInput("r", input)) {
                    removeLimit(scanner);
                } else if (IoUtils.isActualInputMatchesExpectedInput("q", input)) {
                    System.out.println("Going back.");
                    keepAsking = false;
                } else {
                    throw new IllegalArgumentException("You didn't type any valid option.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                scanner.nextLine();
            }
        }
    }

    private void createLimit(Scanner scanner) {
        Session session = sessionService.getSession();
        List<Command> commands = session.getAccount().getType().getLimitableCommands();
        String inputDescriptions = IoUtils.getCommandInputDescriptions(commands);
        System.out.printf("Which command do you want to limit? (%s)%n", inputDescriptions);
        String input = scanner.nextLine();
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
        session.getAccount().addLimit(limit);
        accountDao.save(session.getAccount());
        System.out.println("The limit was created successfully.");
        scanner.nextLine();
    }

    private void editLimit(Scanner scanner) {
        Session session = sessionService.getSession();
        if (session.getAccount().getLimits().isEmpty()) {
            System.out.println("There are no limits to edit.");
            scanner.nextLine();
        } else {
            System.out.printf("What limit do you want to edit? (%s)%n", //
                    IoUtils.getIndexSelectionInputDescriptions(session.getAccount().getLimits()));
            String input = scanner.nextLine();
            int selectedIndex = IoUtils.getSelectedIndex(session.getAccount().getLimits(), input);
            Limit selectedLimit = session.getAccount().getLimits().get(selectedIndex);
            System.out.print("New value: ");
            double value = scanner.nextDouble();
            selectedLimit.setValue(value);
            limitDao.save(selectedLimit);
            System.out.println("The limit was updated successfully.");
            scanner.nextLine();
        }
    }

    private void removeLimit(Scanner scanner) {
        Session session = sessionService.getSession();
        if (session.getAccount().getLimits().isEmpty()) {
            System.out.println("There are no limits to remove.");
            scanner.nextLine();
        } else {
            System.out.printf("What limit do you want to remove? (%s)%n", //
                    IoUtils.getIndexSelectionInputDescriptions(session.getAccount().getLimits()));
            String input = scanner.nextLine();
            int selectedIndex = IoUtils.getSelectedIndex(session.getAccount().getLimits(), input);
            Limit selectedLimit = session.getAccount().getLimits().get(selectedIndex);
            session.getAccount().getLimits().remove(selectedLimit);
            accountDao.save(session.getAccount());
            limitDao.remove(selectedLimit);
            System.out.println("The limit was removed successfully.");
            scanner.nextLine();
        }
    }
}
