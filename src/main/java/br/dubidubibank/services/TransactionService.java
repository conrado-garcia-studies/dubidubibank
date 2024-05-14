package br.dubidubibank.services;

import br.dubidubibank.entities.Account;
import br.dubidubibank.entities.Command;
import br.dubidubibank.entities.Transaction;
import java.util.List;

public interface TransactionService {
  List<Transaction> findByAccount(Account account);

  List<Transaction> findTodaysByCommandAndSourceAccount(Command command, Account sourceAccount);

  Transaction save(Transaction transaction);
}
