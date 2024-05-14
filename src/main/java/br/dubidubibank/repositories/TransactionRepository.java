package br.dubidubibank.repositories;

import br.dubidubibank.entities.Account;
import br.dubidubibank.entities.Command;
import br.dubidubibank.entities.Transaction;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  List<Transaction> findByCommandAndCreationInstantBetweenAndSourceAccount(
      Command command, Instant startInstant, Instant endInstant, Account sourceAccount);

  List<Transaction> findBySourceAccountOrTargetAccountOrderByCreationInstantDesc(
      Account sourceAccount, Account targetAccount);
}
