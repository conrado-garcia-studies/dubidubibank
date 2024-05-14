package br.dubidubibank.clis.impl;

import br.dubidubibank.enums.CommandCode;
import br.dubidubibank.services.SessionService;
import br.dubidubibank.utils.CliUtils;
import java.util.Scanner;

public class LogInCli extends TemplateCli {
  public LogInCli(CliUtils cliUtils, SessionService sessionService) {
    super(cliUtils, sessionService);
  }

  @Override
  protected void executeInternal(Scanner scanner) {
    sessionService.checkPermission(CommandCode.LOG_IN);
    System.out.print("Agency: "); // NOSONAR
    int agencyNumber = scanner.nextInt();
    System.out.print("Account: "); // NOSONAR
    int accountNumber = scanner.nextInt();
    scanner.nextLine();
    System.out.print("Password: "); // NOSONAR
    String password = scanner.nextLine();
    sessionService.logIn(agencyNumber, accountNumber, password);
    System.out.println("You are logged in!"); // NOSONAR
  }
}
