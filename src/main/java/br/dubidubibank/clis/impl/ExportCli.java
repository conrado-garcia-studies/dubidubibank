package br.dubidubibank.clis.impl;

import br.dubidubibank.entities.Account;
import br.dubidubibank.enums.CommandCode;
import br.dubidubibank.services.SessionService;
import br.dubidubibank.services.TransactionExportService;
import br.dubidubibank.utils.CliUtils;
import java.util.Scanner;
import lombok.NonNull;

public class ExportCli extends TemplateCli {
  @NonNull private final TransactionExportService transactionExportService;

  public ExportCli(
      CliUtils cliUtils,
      SessionService sessionService,
      TransactionExportService transactionExportService) {
    super(cliUtils, sessionService);
    this.transactionExportService = transactionExportService;
  }

  @Override
  protected void executeInternal(Scanner scanner) {
    Account account = sessionService.getAccountAndCheckPermission(CommandCode.EXPORT);
    System.out.println( // NOSONAR
        "You can export your transactions to a CSV file, do you wish to do that now? ([Y]es, [N]o)");
    String input = scanner.nextLine();
    if (cliUtils.isActualInputMatchesExpectedInput("y", input)) {
      System.out.println("Exporting transaction report..."); // NOSONAR
      String fileName = transactionExportService.export(account);
      System.out.printf("Report exported successfully! Please check: %s%n", fileName); // NOSONAR
    } else if (cliUtils.isActualInputMatchesExpectedInput("n", input)) {
      System.out.println("You chose not to export a report."); // NOSONAR
    } else {
      throw new IllegalArgumentException("You didn't type any valid option.");
    }
  }
}
