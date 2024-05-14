package br.dubidubibank.clis.impl;

import br.dubidubibank.entities.Account;
import br.dubidubibank.enums.CommandCode;
import br.dubidubibank.services.AccountService;
import br.dubidubibank.services.SessionService;
import br.dubidubibank.utils.CliUtils;
import java.util.Scanner;
import lombok.NonNull;

public class WithdrawCli extends TemplateCli {
  @NonNull private final AccountService accountService;

  public WithdrawCli(
      AccountService accountService, CliUtils cliUtils, SessionService sessionService) {
    super(cliUtils, sessionService);
    this.accountService = accountService;
  }

  @Override
  protected void executeInternal(Scanner scanner) {
    Account account = sessionService.getAccountAndCheckPermission(CommandCode.WITHDRAW);
    System.out.println("How much do you want to withdraw?"); // NOSONAR
    System.out.print("$"); // NOSONAR
    double amount = scanner.nextDouble();
    accountService.withdraw(account, amount);
    System.out.println("The withdraw was successful."); // NOSONAR
    scanner.nextLine();
  }
}
