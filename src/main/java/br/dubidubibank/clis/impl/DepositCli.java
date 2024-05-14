package br.dubidubibank.clis.impl;

import br.dubidubibank.entities.Account;
import br.dubidubibank.enums.CommandCode;
import br.dubidubibank.services.AccountService;
import br.dubidubibank.services.SessionService;
import br.dubidubibank.utils.CliUtils;
import java.util.Scanner;
import lombok.NonNull;

public class DepositCli extends TemplateCli {
  @NonNull private final AccountService accountService;

  public DepositCli(
      AccountService accountService, CliUtils cliUtils, SessionService sessionService) {
    super(cliUtils, sessionService);
    this.accountService = accountService;
  }

  @Override
  protected void executeInternal(Scanner scanner) {
    Account account = sessionService.getAccountAndCheckPermission(CommandCode.DEPOSIT);
    System.out.println("How much do you want to deposit?"); // NOSONAR
    System.out.print("$"); // NOSONAR
    double amount = scanner.nextDouble();
    accountService.deposit(account, amount);
    System.out.println("The deposit was successful."); // NOSONAR
    scanner.nextLine();
  }
}
