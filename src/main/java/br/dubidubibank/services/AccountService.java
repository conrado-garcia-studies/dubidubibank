package br.dubidubibank.services;

import br.dubidubibank.entities.Account;
import java.util.Optional;
import org.springframework.lang.Nullable;

public interface AccountService {
  Account deposit(Account account, double amount);

  Account findAnonymous();

  Account findByAgencyNumberAndNumber(int agencyNumber, int number);

  Account findById(Long id);

  Optional<Account> findOptionalByAgencyNumberAndNumber(int agencyNumber, int number);

  Account patch(
      Account account,
      Account patch,
      @Nullable Integer targetAccountAgencyNumber,
      @Nullable Integer targetAccountNumber);

  Account save(Account account);

  Account transfer(double amount, Account sourceAccount, Account targetAccount);

  Account withdraw(Account account, double amount);
}
