package br.dubidubibank.clis.impl;

import br.dubidubibank.enums.CommandCode;
import br.dubidubibank.services.SessionService;
import br.dubidubibank.utils.CliUtils;
import java.util.Scanner;

public class LogOutCli extends TemplateCli {
  public LogOutCli(CliUtils cliUtils, SessionService sessionService) {
    super(cliUtils, sessionService);
  }

  @Override
  protected void executeInternal(Scanner scanner) {
    sessionService.checkPermission(CommandCode.LOG_OUT);
    sessionService.logOut();
    System.out.println("You are logged out!"); // NOSONAR
  }
}
