package br.dubidubibank.services.impl;

import br.dubidubibank.entities.*;
import br.dubidubibank.enums.AccountTypeCode;
import br.dubidubibank.enums.CommandCode;
import br.dubidubibank.exceptions.ResourceNotFoundException;
import br.dubidubibank.repositories.AccountRepository;
import br.dubidubibank.services.*;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultAccountService implements AccountService, UserDetailsService {
  @NonNull private AccountTypeService accountTypeService;
  @NonNull private CommandService commandService;
  @NonNull private AccountRepository repository;
  @NonNull private RestrictionService restrictionService;
  @NonNull private TransactionService transactionService;

  @Override
  public Account deposit(Account account, double amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("You cannot deposit a zero or negative amount.");
    }
    Command command = commandService.findByCode(CommandCode.DEPOSIT);
    Optional<Restriction> activeRestriction =
        restrictionService.findActiveByAccountAndCommand(account, command);
    if (activeRestriction.isPresent()) {
      List<Transaction> transactions =
          transactionService.findTodaysByCommandAndSourceAccount(command, account);
      double depositedAmountToday =
          transactions.stream() //
              .map(Transaction::getAmount) //
              .mapToDouble(Double::doubleValue) //
              .sum();
      if (amount + depositedAmountToday > activeRestriction.get().getAmount()) {
        throw new IllegalArgumentException(
            String.format(
                "The amount you chose plus the amount you deposited today of $%.2f exceeds the current restriction of"
                    + " $%.2f, which goes from %s to %s.", // NOSONAR
                depositedAmountToday,
                activeRestriction.get().getAmount(),
                activeRestriction.get().getStartTime(),
                activeRestriction.get().getEndTime()));
      }
    }
    account.deposit(amount);
    repository.save(account);
    Transaction transaction = new Transaction(amount, command, account, account);
    transactionService.save(transaction);
    return account;
  }

  @Override
  public Account findAnonymous() {
    AccountTypeCode typeCode = AccountTypeCode.ANONYMOUS;
    AccountType anonymousType = accountTypeService.findByCode(typeCode);
    return repository
        .findByType(anonymousType)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    String.format("Could not find account with type code %s.", typeCode)));
  }

  @Override
  public Account findByAgencyNumberAndNumber(int agencyNumber, int number) {
    return findOptionalByAgencyNumberAndNumber(agencyNumber, number)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    String.format(
                        "Could not find account by agency number %s and number %s.",
                        agencyNumber, number)));
  }

  @Override
  public Account findById(Long id) {
    return repository
        .findById(id)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    String.format("Could not find account by identifier %s.", id)));
  }

  @Override
  public Optional<Account> findOptionalByAgencyNumberAndNumber(int agencyNumber, int number) {
    return repository.findByAgencyNumberAndNumber(agencyNumber, number);
  }

  @Override
  public Account patch(
      Account account,
      Account patch,
      @Nullable Integer targetAccountAgencyNumber,
      @Nullable Integer targetAccountNumber) {
    if (targetAccountAgencyNumber == null ^ targetAccountNumber == null) {
      throw new IllegalArgumentException(
          "Target account agency number and number must either be both null or both not null.");
    }
    if (targetAccountAgencyNumber != null) {
      if (patch.getBalance() < account.getBalance()) {
        Account targetAccount =
            findByAgencyNumberAndNumber(targetAccountAgencyNumber, targetAccountNumber);
        account = transfer(account.getBalance() - patch.getBalance(), account, targetAccount);
        return account;
      }
      throw new IllegalArgumentException("You cannot transfer a zero or negative value.");
    } else {
      if (patch.getBalance() > account.getBalance()) {
        account = deposit(account, patch.getBalance() - account.getBalance());
        return account;
      } else if (patch.getBalance() < account.getBalance()) {
        account = withdraw(account, account.getBalance() - patch.getBalance());
        return account;
      }
      throw new IllegalArgumentException("You cannot deposit or withdraw a zero amount.");
    }
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      String[] agencyNumberAndNumber = username.split("\\.");
      int agencyNumber = Integer.parseInt(agencyNumberAndNumber[0]);
      int number = Integer.parseInt(agencyNumberAndNumber[1]);
      return repository
          .findByAgencyNumberAndNumber(agencyNumber, number)
          .orElseThrow(
              () ->
                  new UsernameNotFoundException(
                      String.format(
                          "Could not find account by agency number %s and number %s.",
                          agencyNumber, number)));
    } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
      throw new UsernameNotFoundException(
          String.format(
              "Could not find account by username %s because it does not follow the format %%d.%%d.",
              username));
    }
  }

  @Override
  public Account save(Account account) {
    return repository.save(account);
  }

  @Override
  @Transactional
  public Account transfer(double amount, Account sourceAccount, Account targetAccount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("You cannot transfer a zero or negative amount.");
    }
    Command command = commandService.findByCode(CommandCode.TRANSFER);
    Optional<Restriction> activeRestriction =
        restrictionService.findActiveByAccountAndCommand(sourceAccount, command);
    if (activeRestriction.isPresent()) {
      List<Transaction> transactions =
          transactionService.findTodaysByCommandAndSourceAccount(command, sourceAccount);
      double transferredAmountToday =
          transactions.stream() //
              .map(Transaction::getAmount) //
              .mapToDouble(Double::doubleValue) //
              .sum();
      if (amount + transferredAmountToday > activeRestriction.get().getAmount()) {
        throw new IllegalArgumentException(
            String.format(
                "The amount you chose plus the amount you transferred today of $%.2f exceeds the current restriction of"
                    + " $%.2f, which goes from %s to %s.",
                transferredAmountToday,
                activeRestriction.get().getAmount(),
                activeRestriction.get().getStartTime(),
                activeRestriction.get().getEndTime()));
      }
    }
    sourceAccount.withdraw(amount);
    targetAccount.deposit(amount);
    repository.saveAll(List.of(sourceAccount, targetAccount));
    Transaction transaction = new Transaction(amount, command, sourceAccount, targetAccount);
    transactionService.save(transaction);
    return sourceAccount;
  }

  @Override
  public Account withdraw(Account account, double amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("You cannot withdraw a zero or negative amount.");
    }
    Command command = commandService.findByCode(CommandCode.WITHDRAW);
    Optional<Restriction> activeRestriction =
        restrictionService.findActiveByAccountAndCommand(account, command);
    if (activeRestriction.isPresent()) {
      List<Transaction> transactions =
          transactionService.findTodaysByCommandAndSourceAccount(command, account);
      double withdrawnAmountToday =
          transactions.stream() //
              .map(Transaction::getAmount) //
              .mapToDouble(Double::doubleValue) //
              .sum();
      if (amount + withdrawnAmountToday > activeRestriction.get().getAmount()) {
        throw new IllegalArgumentException(
            String.format(
                "The amount you chose plus the amount you withdrew today of $%.2f exceeds the current restriction of"
                    + " $%.2f, which goes from %s to %s.",
                withdrawnAmountToday,
                activeRestriction.get().getAmount(),
                activeRestriction.get().getStartTime(),
                activeRestriction.get().getEndTime()));
      }
    }
    account.withdraw(amount);
    repository.save(account);
    Transaction transaction = new Transaction(amount, command, account, account);
    transactionService.save(transaction);
    return account;
  }
}
