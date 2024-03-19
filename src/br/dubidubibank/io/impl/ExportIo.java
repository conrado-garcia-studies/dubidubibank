package br.dubidubibank.io.impl;

import br.dubidubibank.services.SessionService;
import br.dubidubibank.services.TransactionExportService;
import br.dubidubibank.session.Session;
import br.dubidubibank.utils.IoUtils;

import java.util.Scanner;

public class ExportIo extends ScreenTemplateIo {
    private final TransactionExportService transactionExportService;

    public ExportIo(SessionService sessionService, TransactionExportService transactionExportService) {
        super(sessionService);
        this.transactionExportService = transactionExportService;
    }

    @Override
    protected void executeInternal(Scanner scanner) {
        System.out.println("You can export your transactions to a CSV file, do you wish to do that now? ([Y]es, [N]o)");
        String input = scanner.nextLine();
        if (IoUtils.isActualInputMatchesExpectedInput("y", input)) {
            System.out.println("Exporting transaction report...");
            Session session = sessionService.getSession();
            String fileName = transactionExportService.export(session.getAccount());
            System.out.printf("Report exported successfully! Please check: %s%n", fileName);
        } else if (IoUtils.isActualInputMatchesExpectedInput("n", input)) {
            System.out.println("You chose not to export a report.");
        } else {
            throw new IllegalArgumentException("You didn't type any valid option.");
        }
    }
}
