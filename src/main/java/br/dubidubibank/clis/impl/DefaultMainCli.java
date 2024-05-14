package br.dubidubibank.clis.impl;

import br.dubidubibank.clis.MainCli;
import java.util.Locale;
import java.util.Scanner;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultMainCli implements MainCli {
  @NonNull private CommandCli commandCli;

  @Override
  public void start() {
    Locale.setDefault(Locale.US);
    Scanner scanner = new Scanner(System.in);
    commandCli.execute(scanner);
    scanner.close();
  }
}
