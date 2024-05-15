package br.dubidubibank.services.impl;

import br.dubidubibank.entities.Account;
import br.dubidubibank.entities.Transaction;
import br.dubidubibank.enums.CommandCode;
import br.dubidubibank.exceptions.ExportException;
import br.dubidubibank.records.ExportRecord;
import br.dubidubibank.services.SessionService;
import br.dubidubibank.services.TransactionExportService;
import br.dubidubibank.services.TransactionService;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.access.AccessDeniedException;

@RequiredArgsConstructor
public class DefaultTransactionExportService implements TransactionExportService {
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
  @NonNull private SessionService sessionService;
  @NonNull private TransactionService transactionService;

  @Override
  public String export(Account account) {
    String folderName = "./reports";
    try {
      Files.createDirectories(new File(folderName).toPath());
    } catch (IOException e) {
      throw new ExportException("Could not create reports folder.");
    }
    String fileName =
        String.format(
            "%s/transactions-%s-%s-%s.csv",
            folderName,
            account.getAgencyNumber(),
            account.getNumber(),
            DATE_TIME_FORMATTER.format(ZonedDateTime.now(account.getZoneId())));
    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
      List<Transaction> transactions = transactionService.findByAccount(account);
      writeLines(bufferedWriter, transactions);
      return new File(fileName).getAbsolutePath();
    } catch (IOException e) {
      throw new ExportException("Could not generate report.");
    }
  }

  private void writeLines(BufferedWriter bufferedWriter, List<Transaction> transactions)
      throws IOException {
    bufferedWriter.write(
        "Time,Command,Amount,Source Agency,Source Account,Target Agency,Target Account");
    bufferedWriter.newLine();
    if (getAccessDeniedMessage().isPresent()) {
      bufferedWriter.write(String.format("%s,,,,,,", getAccessDeniedMessage().orElse(null)));
    } else {
      for (Transaction transaction : transactions) {
        ZonedDateTime creationDateTime =
            ZonedDateTime.ofInstant(
                transaction.getCreationInstant(), transaction.getSourceAccount().getZoneId());
        String line =
            String.format(
                "%s,%s,%.2f,%s,%s,%s,%s",
                DateTimeFormatter.RFC_1123_DATE_TIME.format(creationDateTime).replace(",", " "),
                transaction.getCommand().getDescription(),
                transaction.getAmount(),
                transaction.getSourceAccount().getAgencyNumber(),
                transaction.getSourceAccount().getNumber(),
                transaction.getTargetAccount().getAgencyNumber(),
                transaction.getTargetAccount().getNumber());
        bufferedWriter.write(line);
        bufferedWriter.newLine();
      }
    }
  }

  private Optional<String> getAccessDeniedMessage() {
    try {
      sessionService.checkPermission(CommandCode.EXPORT);
    } catch (AccessDeniedException e) {
      return Optional.of(Optional.ofNullable(e.getMessage()).orElse(StringUtils.EMPTY));
    }
    return Optional.empty();
  }

  @Override
  public ExportRecord getExportAndDeleteFile(Account account) {
    try {
      String absolutePath = export(account);
      Path path = Paths.get(absolutePath);
      ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
      Files.deleteIfExists(path);
      return new ExportRecord(path, resource);
    } catch (IOException e) {
      throw new ExportException("Could not get resource.");
    }
  }
}
