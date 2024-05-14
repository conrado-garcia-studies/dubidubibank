package br.dubidubibank.clis.impl;

import br.dubidubibank.enums.CommandCode;
import br.dubidubibank.services.SessionService;
import br.dubidubibank.utils.CliUtils;
import java.util.Scanner;

public class QuitCli extends TemplateCli {
  public QuitCli(CliUtils cliUtils, SessionService sessionService) {
    super(cliUtils, sessionService);
  }

  @Override
  protected void executeInternal(Scanner scanner) {
    sessionService.checkPermission(CommandCode.QUIT);
    System.out.println("Thanks for using Dubidubibank!"); // NOSONAR
  }
}
