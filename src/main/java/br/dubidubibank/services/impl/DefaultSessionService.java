package br.dubidubibank.services.impl;

import br.dubidubibank.entities.Account;
import br.dubidubibank.entities.Command;
import br.dubidubibank.enums.AccountTypeCode;
import br.dubidubibank.enums.CommandCode;
import br.dubidubibank.services.AccountService;
import br.dubidubibank.services.SessionService;
import br.dubidubibank.session.Session;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
public class DefaultSessionService implements SessionService {
  @NonNull private AccountService accountService;
  @NonNull private AuthenticationManager authenticationManager;
  private Session session;

  @Override
  public Account getAccount() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Authentication authentication = securityContext.getAuthentication();
    if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
      return accountService.findAnonymous();
    }
    return (Account) authentication.getPrincipal();
  }

  @Override
  public Account getAccountAndCheckPermission(CommandCode commandCode) {
    Account account = getAccount();
    boolean accessDenied =
        account.getAuthorities().stream() //
            .map(Command.class::cast) //
            .map(Command::getCode) //
            .noneMatch(commandCode::equals);
    if (accessDenied) {
      throw new AccessDeniedException(
          String.format("The current account does not have access to %s command.", commandCode));
    }
    return account;
  }

  @Override
  public void checkPermission(CommandCode commandCode) {
    getAccountAndCheckPermission(commandCode);
  }

  @Override
  public String getDescription() {
    Account account = getAccount();
    if (account.getType().getCode() == AccountTypeCode.ANONYMOUS) {
      return account.getDescription();
    }
    return account
        .getLastLogInInstant()
        .map(instant -> ZonedDateTime.ofInstant(instant, account.getZoneId()))
        .map(
            dateTime ->
                String.format(
                    "%s%nLog in time: %s",
                    account.getDescription(),
                    DateTimeFormatter.RFC_1123_DATE_TIME.format(dateTime)))
        .orElseGet(account::getDescription);
  }

  @Override
  public void logIn(int agencyNumber, int accountNumber, String password) {
    String username = String.format("%d.%d", agencyNumber, accountNumber);
    UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(username, password);
    Authentication authentication = authenticationManager.authenticate(token);
    SecurityContext securityContext = SecurityContextHolder.getContext();
    securityContext.setAuthentication(authentication);
    Account account = (Account) authentication.getPrincipal();
    account.setLastLogInInstant(Instant.now());
    accountService.save(account);
  }

  @Override
  public void logOut() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    securityContext.setAuthentication(null);
  }
}
