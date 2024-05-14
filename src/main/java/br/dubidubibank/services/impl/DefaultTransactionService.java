package br.dubidubibank.services.impl;

import br.dubidubibank.entities.Account;
import br.dubidubibank.entities.Command;
import br.dubidubibank.entities.Transaction;
import br.dubidubibank.repositories.TransactionRepository;
import br.dubidubibank.services.TransactionService;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultTransactionService implements TransactionService {
  @NonNull private TransactionRepository repository;

  @Override
  public List<Transaction> findByAccount(Account account) {
    return repository.findBySourceAccountOrTargetAccountOrderByCreationInstantDesc(
        account, account);
  }

  @Override
  public List<Transaction> findTodaysByCommandAndSourceAccount(
      Command command, Account sourceAccount) {
    Instant startInstant =
        ZonedDateTime.now(sourceAccount.getZoneId()).with(LocalTime.MIN).toInstant();
    Instant endInstant = startInstant.plus(1L, ChronoUnit.DAYS).minusMillis(1L);
    return repository.findByCommandAndCreationInstantBetweenAndSourceAccount(
        command, startInstant, endInstant, sourceAccount);
  }

  @Override
  public Transaction save(Transaction transaction) {
    return repository.save(transaction);
  }
}
