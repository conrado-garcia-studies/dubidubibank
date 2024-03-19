package br.dubidubibank.services.impl;

import br.dubidubibank.daos.TransactionDao;
import br.dubidubibank.entities.Account;
import br.dubidubibank.entities.Transaction;
import br.dubidubibank.exceptions.ExportException;
import br.dubidubibank.services.TransactionExportService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DefaultTransactionExportService implements TransactionExportService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    private final TransactionDao transactionDao;

    public DefaultTransactionExportService(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    @Override
    public String export(Account account) {
        if (account.getAgencyNumber().isEmpty() || account.getNumber().isEmpty()) {
            throw new ExportException("The account does not have a number.");
        }
        String folderName = "../reports";
        try {
            Files.createDirectories(new File(folderName).toPath());
        } catch (IOException e) {
            throw new ExportException("Could not create reports folder.");
        }
        String fileName = String.format("%s/transactions-%s-%s-%s.csv", folderName, account.getAgencyNumber().get(), //
                account.getNumber().get(), DATE_TIME_FORMATTER.format(ZonedDateTime.now()));
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            List<Transaction> transactions = transactionDao.get(account);
            writeLines(bufferedWriter, transactions);
            return new File(fileName).getAbsolutePath();
        } catch (IOException e) {
            throw new ExportException("Could not generate report.");
        }
    }

    private void writeLines(BufferedWriter bufferedWriter, List<Transaction> transactions) throws IOException {
        bufferedWriter.write("Time,Command,Value,Source Agency,Source Account,Target Agency,Target Account");
        bufferedWriter.newLine();
        for (Transaction transaction : transactions) {
            if (transaction.getSourceAccount().getAgencyNumber().isPresent() //
                    && transaction.getSourceAccount().getNumber().isPresent() //
                    && transaction.getTargetAccount().getAgencyNumber().isPresent() //
                    && transaction.getTargetAccount().getNumber().isPresent()) {
                String line = String.format( //
                        "%s,%s,%.2f,%s,%s,%s,%s", //
                        DateTimeFormatter.RFC_1123_DATE_TIME.format(transaction.getCreationTime()) //
                                .replace(",", " "), //
                        transaction.getCommand(), //
                        transaction.getValue(), //
                        transaction.getSourceAccount().getAgencyNumber().get(), //
                        transaction.getSourceAccount().getNumber().get(), //
                        transaction.getTargetAccount().getAgencyNumber().get(), //
                        transaction.getTargetAccount().getNumber().get());
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        }
    }
}
