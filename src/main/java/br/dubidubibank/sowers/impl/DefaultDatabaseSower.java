package br.dubidubibank.sowers.impl;

import br.dubidubibank.entities.*;
import br.dubidubibank.enums.AccountTypeCode;
import br.dubidubibank.enums.CommandCode;
import br.dubidubibank.repositories.*;
import br.dubidubibank.sowers.DatabaseSower;
import br.dubidubibank.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultDatabaseSower implements DatabaseSower {
  @NonNull private AccountRepository accountRepository;
  @NonNull private AccountTypeRepository accountTypeRepository;
  @NonNull private CommandRepository commandRepository;
  @NonNull private PersonRepository personRepository;
  @NonNull private RestrictionRepository restrictionRepository;
  @NonNull private SecurityUtils securityUtils;

  @Override
  @Transactional
  public void seed() {
    Command depositCommand =
        new Command(
            "depositCli", //
            "d", //
            "[D]eposit", //
            CommandCode.DEPOSIT, //
            "Deposit", //
            true, //
            false);
    Command exportCommand =
        new Command(
            "exportCli", //
            "e", //
            "[E]xport", //
            CommandCode.EXPORT, //
            "Export", //
            false, //
            false);
    Command logInCommand =
        new Command(
            "logInCli", //
            "l", //
            "[L]og in", //
            CommandCode.LOG_IN, //
            "Log in", //
            false, //
            false);
    Command logOutCommand =
        new Command(
            "logOutCli", //
            "l", //
            "[L]og out", //
            CommandCode.LOG_OUT, //
            "Log out", //
            false, //
            false);
    Command openCommand =
        new Command(
            "openCli", //
            "o", //
            "[O]pen", //
            CommandCode.OPEN, //
            "Open", //
            false, //
            false);
    Command quitCommand =
        new Command(
            "quitCli", //
            "q", //
            "[Q]uit", //
            CommandCode.QUIT, //
            "Quit", //
            false, //
            true);
    Command restrictCommand =
        new Command(
            "restrictCli", //
            "r", //
            "[R]estrict", //
            CommandCode.RESTRICT, //
            "Restrict", //
            false, //
            false);
    Command transferCommand =
        new Command(
            "transferCli", //
            "t", //
            "[T]ransfer", //
            CommandCode.TRANSFER, //
            "Transfer", //
            true, //
            false);
    Command withdrawCommand =
        new Command(
            "withdrawCli", //
            "w", //
            "[W]ithdraw", //
            CommandCode.WITHDRAW, //
            "Withdraw", //
            true, //
            false);
    commandRepository.saveAll(
        List.of(
            depositCommand, //
            exportCommand, //
            logInCommand, //
            logOutCommand, //
            openCommand, //
            quitCommand, //
            restrictCommand, //
            transferCommand, //
            withdrawCommand));
    AccountType adminAccountType =
        new AccountType(
            AccountTypeCode.ADMIN, //
            "Admin");
    adminAccountType.addCommand(withdrawCommand);
    adminAccountType.addCommand(depositCommand);
    adminAccountType.addCommand(transferCommand);
    adminAccountType.addCommand(restrictCommand);
    adminAccountType.addCommand(exportCommand);
    adminAccountType.addCommand(openCommand);
    adminAccountType.addCommand(logOutCommand);
    adminAccountType.addCommand(quitCommand);
    adminAccountType.setCliInput("a");
    adminAccountType.setCliInputDescription("[A]dmin");
    AccountType anonymousAccountType =
        new AccountType(
            AccountTypeCode.ANONYMOUS, //
            "Anonymous");
    anonymousAccountType.addCommand(logInCommand);
    anonymousAccountType.addCommand(quitCommand);
    AccountType checkingAccountType =
        new AccountType(
            AccountTypeCode.CHECKING, //
            "Checking");
    checkingAccountType.addCommand(withdrawCommand);
    checkingAccountType.addCommand(depositCommand);
    checkingAccountType.addCommand(transferCommand);
    checkingAccountType.addCommand(restrictCommand);
    checkingAccountType.addCommand(exportCommand);
    checkingAccountType.addCommand(logOutCommand);
    checkingAccountType.addCommand(quitCommand);
    checkingAccountType.setCliInput("c");
    checkingAccountType.setCliInputDescription("[C]hecking");
    AccountType savingsAccountType =
        new AccountType(
            AccountTypeCode.SAVINGS, //
            "Savings");
    savingsAccountType.addCommand(withdrawCommand);
    savingsAccountType.addCommand(depositCommand);
    savingsAccountType.addCommand(restrictCommand);
    savingsAccountType.addCommand(logOutCommand);
    savingsAccountType.addCommand(quitCommand);
    savingsAccountType.setCliInput("s");
    savingsAccountType.setCliInputDescription("[S]avings");
    accountTypeRepository.saveAll(
        List.of(
            adminAccountType, //
            anonymousAccountType, //
            checkingAccountType, //
            savingsAccountType));
    Person adminPerson =
        new Person(
            "christell.rodriguez@gmail.com", //
            "Christell");
    Person anonymousPerson =
        new Person(
            "anonymous@dubidubibank.br", //
            "Anonymous");
    Person checkingPerson =
        new Person(
            "charlie1984@hotmail.com", //
            "Charlie Schmidt");
    Person savingsPerson =
        new Person(
            "pedro.pedro.pedro.pedro.pe.official@gmail.com", //
            "Pedro the Racoon");
    personRepository.saveAll(
        List.of(
            adminPerson, //
            anonymousPerson, //
            checkingPerson, //
            savingsPerson));
    ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
    Account adminAccount =
        new Account(
            1, //
            3454.46d, //
            true, //
            1, //
            securityUtils.encodeAndPrefix("admin"), // NOSONAR
            adminPerson, //
            adminAccountType, //
            zoneId);
    Account anonymousAccount =
        new Account(
            0, //
            0d, //
            false, //
            0, //
            securityUtils.encodeAndPrefix("anonymous"), // NOSONAR
            anonymousPerson, //
            anonymousAccountType, //
            zoneId);
    Account checkingAccount =
        new Account(
            8, //
            4234.27d, //
            true, //
            4, //
            securityUtils.encodeAndPrefix("checking"), // NOSONAR
            checkingPerson,
            checkingAccountType, //
            zoneId);
    Account savingsAccount =
        new Account(
            4, //
            1054.32d, //
            true, //
            4, //
            securityUtils.encodeAndPrefix("savings"), // NOSONAR
            savingsPerson, //
            savingsAccountType, //
            zoneId);
    accountRepository.saveAll(
        List.of(
            adminAccount, //
            anonymousAccount, //
            checkingAccount, //
            savingsAccount));
    Restriction lunchTimeDepositRestriction =
        new Restriction(
            adminAccount, //
            123.45d, //
            depositCommand, //
            LocalTime.of(13, 37, 59, 999_000_000), // ,
            LocalTime.of(11, 37));
    Restriction lunchTimeTransferRestriction =
        new Restriction(
            checkingAccount, //
            1234.56d, //
            transferCommand, //
            LocalTime.of(13, 30, 59, 999_000_000), //
            LocalTime.of(11, 30));
    Restriction lunchTimeWithdrawRestriction =
        new Restriction(
            savingsAccount, //
            12.34d, //
            withdrawCommand, //
            LocalTime.of(13, 30, 59, 999_000_000), //
            LocalTime.of(11, 30));
    Restriction partyTimeDepositRestriction =
        new Restriction(
            savingsAccount, //
            12.34d, //
            depositCommand, //
            LocalTime.of(23, 57, 59, 999_000_000), //
            LocalTime.of(22, 47));
    Restriction partyTimeTransferRestriction =
        new Restriction(
            adminAccount, //
            123.45d, //
            transferCommand, //
            LocalTime.of(23, 55, 59, 999_000_000), //
            LocalTime.of(22, 45));
    Restriction partyTimeWithdrawRestriction =
        new Restriction(
            checkingAccount, //
            1.23d, //
            withdrawCommand, //
            LocalTime.of(23, 55, 59, 999_000_000), //
            LocalTime.of(22, 45));
    restrictionRepository.saveAll(
        List.of(
            lunchTimeDepositRestriction, //
            lunchTimeTransferRestriction, //
            lunchTimeWithdrawRestriction, //
            partyTimeDepositRestriction, //
            partyTimeTransferRestriction, //
            partyTimeWithdrawRestriction));
  }
}
