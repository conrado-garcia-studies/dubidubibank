package br.dubidubibank.io.impl;

import br.dubidubibank.entities.Command;
import br.dubidubibank.enums.CommandCode;
import br.dubidubibank.exceptions.ExportException;
import br.dubidubibank.io.Io;
import br.dubidubibank.services.SessionService;
import br.dubidubibank.session.Session;
import br.dubidubibank.utils.IoUtils;

import java.util.*;

public class CommandIo extends ScreenTemplateIo {
    private final Map<CommandCode, Io> commandCodeToIo;

    public CommandIo(Map<CommandCode, Io> commandCodeToIo, SessionService sessionService) {
        super(sessionService);
        this.commandCodeToIo = new HashMap<>(commandCodeToIo);
    }

    @Override
    public void executeInternal(Scanner scanner) {
        try {
            boolean terminal = false;
            while (!terminal) {
                Session session = sessionService.getSession();
                List<Command> commands = session.getAccount().getType().getCommands();
                if (commands.isEmpty()) {
                    throw new NoSuchElementException(String.format("There should be at least one command for %s" //
                            + " account type. Please contact the administrator.", session.getAccount().getType()));
                }
                super.beforeExecute(scanner);
                String inputDescriptions = IoUtils.getCommandInputDescriptions(commands);
                System.out.printf("Please type a command (%s): ", inputDescriptions);
                try {
                    String input = scanner.nextLine();
                    Command command = IoUtils.getCommand(commands, input);
                    Io io = commandCodeToIo.get(command.getCode());
                    if (io == null) {
                        throw new UnsupportedOperationException("Sorry, we didn't implement that functionality yet." //
                                + " Please contact the administrator.");
                    }
                    io.execute(scanner);
                    terminal = command.getTerminal();
                } catch (InputMismatchException e) {
                    System.out.println("Looks like you typed something wrong. Please try again.");
                    scanner.nextLine();
                    scanner.nextLine();
                } catch (ExportException | IllegalArgumentException | NoSuchElementException |
                         UnsupportedOperationException e) {
                    System.out.println(e.getMessage());
                    scanner.nextLine();
                    scanner.nextLine();
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            scanner.close();
            return;
        }
    }

    @Override
    protected void beforeExecute(Scanner scanner) {
        //Do nothing.
    }

    @Override
    protected void afterExecute(Scanner scanner) {
        //Do nothing.
    }
}
