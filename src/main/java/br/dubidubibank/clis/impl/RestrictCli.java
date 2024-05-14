package br.dubidubibank.clis.impl;

import br.dubidubibank.entities.Account;
import br.dubidubibank.entities.Command;
import br.dubidubibank.entities.Restriction;
import br.dubidubibank.enums.CommandCode;
import br.dubidubibank.services.AccountService;
import br.dubidubibank.services.RestrictionService;
import br.dubidubibank.services.SessionService;
import br.dubidubibank.utils.CliUtils;
import java.time.LocalTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.transaction.TransactionSystemException;

public class RestrictCli extends TemplateCli {
  @NonNull private final AccountService accountService;
  @NonNull private final RestrictionService restrictionService;

  public RestrictCli(
      AccountService accountService,
      CliUtils cliUtils,
      RestrictionService restrictionService,
      SessionService sessionService) {
    super(cliUtils, sessionService);
    this.accountService = accountService;
    this.restrictionService = restrictionService;
  }

  @Override
  protected void executeInternal(Scanner scanner) { // NOSONAR
    sessionService.checkPermission(CommandCode.RESTRICT);
    boolean keepAsking = true;
    boolean first = true;
    while (keepAsking) {
      try {
        System.out.printf( // NOSONAR
            "What do you want to do%s? ([C]reate, [R]ead, [U]pdate, [D]elete, [Q]uit)%n",
            first ? "" : " now");
        String input = scanner.nextLine();
        if (cliUtils.isActualInputMatchesExpectedInput("c", input)) {
          create(scanner);
        } else if (cliUtils.isActualInputMatchesExpectedInput("r", input)) {
          read();
        } else if (cliUtils.isActualInputMatchesExpectedInput("u", input)) {
          update(scanner);
        } else if (cliUtils.isActualInputMatchesExpectedInput("d", input)) {
          delete(scanner);
        } else if (cliUtils.isActualInputMatchesExpectedInput("q", input)) {
          System.out.println("You chose to quit."); // NOSONAR
          keepAsking = false;
        } else {
          throw new IllegalArgumentException("You didn't type any valid option.");
        }
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage()); // NOSONAR
      } catch (InputMismatchException e) {
        System.out.println("Looks like you typed something wrong. Please try again."); // NOSONAR
      } catch (TransactionSystemException e) {
        System.out.println( // NOSONAR
            "Something went wrong. Maybe you typed something wrong? Please try again.");
      }
      if (keepAsking) {
        scanner.nextLine();
      }
      first = false;
    }
  }

  private void create(Scanner scanner) {
    Account account = sessionService.getAccountAndCheckPermission(CommandCode.RESTRICT);
    List<Command> commands = cliUtils.findRestrictableCommands(account.getType().getCommands());
    String inputDescriptions = cliUtils.findCommandInputDescriptions(commands);
    System.out.printf( // NOSONAR
        "Which command do you want to restrict? (%s)%n", inputDescriptions); // NOSONAR
    String input = scanner.nextLine();
    Command command = cliUtils.findCommand(commands, input);
    System.out.print( // NOSONAR
        "Start time (please type in the format HH:mm, e.g. 09:32, 23:37, etc.): "); // NOSONAR
    String startTimestamp = scanner.nextLine();
    System.out.print( // NOSONAR
        "End time (please type in the format HH:mm, e.g. 09:32,  23:37, etc.): "); // NOSONAR
    String endTimestamp = scanner.nextLine();
    System.out.print("Amount: $"); // NOSONAR
    Double amount = scanner.nextDouble();
    LocalTime endTime = cliUtils.toEndLocalTime(endTimestamp);
    LocalTime startTime = cliUtils.toStartLocalTime(startTimestamp);
    Restriction restriction = new Restriction(account, amount, command, endTime, startTime);
    account.addRestriction(restriction);
    restrictionService.save(restriction);
    accountService.save(account);
    System.out.println("The restriction was created successfully.\n"); // NOSONAR
  }

  private void read() {
    Account account = sessionService.getAccountAndCheckPermission(CommandCode.RESTRICT);
    if (account.getRestrictions().isEmpty()) {
      System.out.println("There are no restrictions to read."); // NOSONAR
    } else {
      String restrictionDescriptions =
          account.getRestrictions().stream()
              .map(Restriction::getDescription)
              .collect(Collectors.joining("\n"));
      System.out.printf("These are the restrictions:%n%s%n", restrictionDescriptions); // NOSONAR
    }
  }

  private void update(Scanner scanner) {
    Account account = sessionService.getAccountAndCheckPermission(CommandCode.RESTRICT);
    if (account.getRestrictions().isEmpty()) {
      System.out.println("There are no restrictions to update."); // NOSONAR
    } else {
      System.out.printf( // NOSONAR
          "Which restriction do you want to update? (%s)%n",
          cliUtils.findIndexSelectionInputDescriptions(account.getRestrictions()));
      String input = scanner.nextLine();
      int selectedIndex = cliUtils.findSelectedIndex(account.getRestrictions(), input);
      Restriction selectedRestriction = account.getRestrictions().get(selectedIndex);
      System.out.print("New amount: $"); // NOSONAR
      double amount = scanner.nextDouble();
      selectedRestriction.setAmount(amount);
      restrictionService.save(selectedRestriction);
      System.out.println("The restriction was updated successfully.\n"); // NOSONAR
    }
  }

  private void delete(Scanner scanner) {
    Account account = sessionService.getAccountAndCheckPermission(CommandCode.RESTRICT);
    if (account.getRestrictions().isEmpty()) {
      System.out.println("There are no restrictions to delete."); // NOSONAR
    } else {
      System.out.printf( // NOSONAR
          "Which restriction do you want to delete? (%s)%n",
          cliUtils.findIndexSelectionInputDescriptions(account.getRestrictions()));
      String input = scanner.nextLine();
      int selectedIndex = cliUtils.findSelectedIndex(account.getRestrictions(), input);
      Restriction selectedRestriction = account.getRestrictions().get(selectedIndex);
      account.removeRestriction(selectedRestriction);
      accountService.save(account);
      System.out.println("The restriction was deleted successfully."); // NOSONAR
    }
  }
}
