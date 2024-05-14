package br.dubidubibank.clis.impl;

import br.dubidubibank.entities.*;
import br.dubidubibank.enums.CommandCode;
import br.dubidubibank.services.*;
import br.dubidubibank.utils.CliUtils;
import br.dubidubibank.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import lombok.NonNull;
import org.springframework.transaction.TransactionSystemException;

public class OpenCli extends TemplateCli {
  @NonNull private final AccountService accountService;
  @NonNull private final AccountTypeService accountTypeService;
  @NonNull private final PersonService personService;
  @NonNull private final RestrictionService restrictionService;
  @NonNull private final SecurityUtils securityUtils;

  public OpenCli(
      AccountService accountService,
      AccountTypeService accountTypeService,
      CliUtils cliUtils,
      PersonService personService,
      RestrictionService restrictionService,
      SecurityUtils securityUtils,
      SessionService sessionService) {
    super(cliUtils, sessionService);
    this.accountService = accountService;
    this.accountTypeService = accountTypeService;
    this.personService = personService;
    this.restrictionService = restrictionService;
    this.securityUtils = securityUtils;
  }

  @Override
  @Transactional
  protected void executeInternal(Scanner scanner) {
    sessionService.checkPermission(CommandCode.OPEN);
    System.out.println("Please type the information of new account you want to open."); // NOSONAR
    System.out.print("Agency (it must be a positive number): "); // NOSONAR
    int agencyNumber = scanner.nextInt();
    System.out.print("Account (it must be a positive number): "); // NOSONAR
    int number = scanner.nextInt();
    scanner.nextLine();
    Optional<Account> existingAccount =
        accountService.findOptionalByAgencyNumberAndNumber(agencyNumber, number);
    if (existingAccount.isPresent()) {
      throw new IllegalArgumentException(
          "There is already an account with that number for that agency.");
    }
    Collection<AccountType> accountTypes = accountTypeService.findSelectable();
    String inputDescriptions = cliUtils.findAccountTypeInputDescriptions(accountTypes);
    System.out.printf("What is the account type? (%s)%n", inputDescriptions); // NOSONAR
    String input = scanner.nextLine();
    AccountType accountType = cliUtils.findAccountType(accountTypes, input);
    Person person = createPerson(scanner);
    System.out.print("Password: "); // NOSONAR
    String rawPassword = scanner.nextLine();
    String password = securityUtils.encodeAndPrefix(rawPassword);
    Account account =
        new Account(
            agencyNumber, 0d, true, number, password, person, accountType, ZoneId.systemDefault());
    List<Command> commands = cliUtils.findRestrictableCommands(account.getType().getCommands());
    if (!commands.isEmpty()) {
      accountService.save(account);
      createRestrictions(scanner, account, commands);
    }
    if (account.getId().isEmpty()) {
      accountService.save(account);
    }
    System.out.println("The account was created successfully!"); // NOSONAR
  }

  private Person createPerson(Scanner scanner) {
    System.out.print("Email: "); // NOSONAR
    String email = scanner.nextLine();
    return personService
        .findOptionalByEmail(email)
        .orElseGet(
            () -> {
              System.out.print("Name: "); // NOSONAR
              String name = scanner.nextLine();
              Person person = new Person(email, name);
              personService.save(person);
              return person;
            });
  }

  private void createRestrictions(Scanner scanner, Account account, List<Command> commands) {
    boolean keepAsking = true;
    while (keepAsking) {
      try {
        System.out.printf( // NOSONAR
            "Do you want to create a%s restriction? ([Y]es, [N]o)%n",
            account.getRestrictions().isEmpty() ? "" : " new");
        String input = scanner.nextLine();
        if (cliUtils.isActualInputMatchesExpectedInput("n", input)) {
          keepAsking = false;
        } else if (cliUtils.isActualInputMatchesExpectedInput("y", input)) {
          String inputDescriptions = cliUtils.findCommandInputDescriptions(commands);
          System.out.printf( // NOSONAR
              "Which command do you want to restrict? (%s)%n", inputDescriptions); // NOSONAR
          input = scanner.nextLine();
          Command command = cliUtils.findCommand(commands, input);
          System.out.print( // NOSONAR
              "Start time (please type in the format HH:mm, e.g. 09:32, 23:37, etc.): ");
          String startTimestamp = scanner.nextLine();
          System.out.print( // NOSONAR
              "End time (please type in the format HH:mm, e.g. 09:32,  23:37, etc.): ");
          String endTimestamp = scanner.nextLine();
          System.out.print("Amount: $"); // NOSONAR
          Double amount = scanner.nextDouble();
          LocalTime endTime = cliUtils.toEndLocalTime(endTimestamp);
          LocalTime startTime = cliUtils.toStartLocalTime(startTimestamp);
          Restriction restriction = new Restriction(account, amount, command, endTime, startTime);
          account.addRestriction(restriction);
          restrictionService.save(restriction);
          accountService.save(account);
          System.out.println("The restriction was created successfully."); // NOSONAR
          scanner.nextLine();
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
    }
  }
}
