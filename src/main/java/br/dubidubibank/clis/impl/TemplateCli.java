package br.dubidubibank.clis.impl;

import br.dubidubibank.clis.Cli;
import br.dubidubibank.services.SessionService;
import br.dubidubibank.utils.CliUtils;
import java.util.Scanner;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class TemplateCli implements Cli {
  @NonNull protected CliUtils cliUtils;
  @NonNull protected SessionService sessionService;

  @Override
  public void execute(Scanner scanner) {
    beforeExecute(scanner);
    executeInternal(scanner);
    afterExecute(scanner);
  }

  protected void beforeExecute(Scanner scanner) { // NOSONAR
    clearScreen();
    System.out.println("Dubidubibank"); // NOSONAR
    System.out.println(sessionService.getDescription()); // NOSONAR
    System.out.println(); // NOSONAR
  }

  private void clearScreen() {
    System.out.print("\033[H\033[2J"); // NOSONAR
    System.out.flush(); // NOSONAR
  }

  protected abstract void executeInternal(Scanner scanner);

  protected void afterExecute(Scanner scanner) {
    scanner.nextLine();
  }
}
