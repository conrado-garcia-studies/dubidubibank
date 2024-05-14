package br.dubidubibank.clis.impl;

import br.dubidubibank.entities.Account;
import br.dubidubibank.enums.CommandCode;
import br.dubidubibank.services.AccountService;
import br.dubidubibank.services.SessionService;
import br.dubidubibank.utils.CliUtils;
import java.util.Scanner;
import lombok.NonNull;

public class TransferCli extends TemplateCli {
  @NonNull private final AccountService accountService;

  public TransferCli(
      AccountService accountService, CliUtils cliUtils, SessionService sessionService) {
    super(cliUtils, sessionService);
    this.accountService = accountService;
  }

  @Override
  protected void executeInternal(Scanner scanner) {
    Account account = sessionService.getAccountAndCheckPermission(CommandCode.TRANSFER);
    System.out.println( // NOSONAR
        "Please type the information of the account you want to transfer to."); // NOSONAR
    System.out.print("Agency: "); // NOSONAR
    int agencyNumber = scanner.nextInt();
    System.out.print("Account: "); // NOSONAR
    int number = scanner.nextInt();
    scanner.nextLine();
    Account targetAccount = accountService.findByAgencyNumberAndNumber(agencyNumber, number);
    System.out.println("How much do you want to transfer?"); // NOSONAR
    System.out.print("$"); // NOSONAR
    double amount = scanner.nextDouble();
    accountService.transfer(amount, account, targetAccount);
    System.out.println("The transfer was successful."); // NOSONAR
    scanner.nextLine();
  }
}
