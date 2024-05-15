package br.dubidubibank.services;

import br.dubidubibank.entities.Account;
import br.dubidubibank.records.ExportRecord;

public interface TransactionExportService {
  String export(Account account);

  ExportRecord getExportAndDeleteFile(Account account);
}
