package br.dubidubibank.services;

import br.dubidubibank.entities.Account;
import br.dubidubibank.records.ExportRecord;

public interface TransactionExportService {
  ExportRecord getExportAndDeleteFile(Account account);

  String export(Account account);
}
