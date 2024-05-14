package br.dubidubibank.clis.impl;

import br.dubidubibank.clis.Cli;
import br.dubidubibank.entities.Account;
import br.dubidubibank.entities.Command;
import br.dubidubibank.exceptions.ExportException;
import br.dubidubibank.exceptions.ResourceNotFoundException;
import br.dubidubibank.services.SessionService;
import br.dubidubibank.utils.CliUtils;
import java.util.*;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.TransactionSystemException;

public class CommandCli extends TemplateCli {
  @NonNull private final ApplicationContext applicationContext;

  public CommandCli(
      ApplicationContext applicationContext, CliUtils cliUtils, SessionService sessionService) {
    super(cliUtils, sessionService);
    this.applicationContext = applicationContext;
  }

  @Override
  public void executeInternal(Scanner scanner) {
    try {
      executeCommands(scanner);
    } catch (ResourceNotFoundException e) {
      System.out.println(e.getMessage()); // NOSONAR
      scanner.close();
    }
  }

  private void executeCommands(Scanner scanner) {
    boolean terminal = false;
    while (!terminal) {
      Account account = sessionService.getAccount();
      List<Command> commands = account.getType().getCommands();
      super.beforeExecute(scanner);
      String inputDescriptions = cliUtils.findCommandInputDescriptions(commands);
      System.out.printf("Please type a command (%s): ", inputDescriptions); // NOSONAR
      try {
        String input = scanner.nextLine();
        Command command = cliUtils.findCommand(commands, input);
        Cli cli = applicationContext.getBean(command.getCliBeanCode(), Cli.class);
        cli.execute(scanner);
        terminal = command.getTerminal();
      } catch (AccessDeniedException e) {
        System.out.println("Your session expired. Please log in again."); // NOSONAR
        scanner.nextLine();
      } catch (BadCredentialsException e) {
        System.out.println( // NOSONAR
            "Looks like you typed the password wrong. Please try again.");
        scanner.nextLine();
      } catch (BeansException e) {
        System.out.println( // NOSONAR
            "Sorry, we didn't implement that functionality yet. Please contact the administrator.");
        scanner.nextLine();
      } catch (InputMismatchException e) {
        System.out.println("Looks like you typed something wrong. Please try again."); // NOSONAR
        scanner.nextLine();
      } catch (TransactionSystemException e) {
        System.out.println( // NOSONAR
            "Something went wrong. Maybe you typed something wrong? Please try again.");
        scanner.nextLine();
      } catch (ExportException
          | IllegalArgumentException
          | ResourceNotFoundException
          | UnsupportedOperationException e) {
        System.out.println(e.getMessage()); // NOSONAR
        scanner.nextLine();
      }
    }
  }

  @Override
  protected void beforeExecute(Scanner scanner) {
    // Do nothing.
  }

  @Override
  protected void afterExecute(Scanner scanner) {
    // Do nothing.
  }
}
