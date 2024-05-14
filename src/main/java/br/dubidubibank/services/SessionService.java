package br.dubidubibank.services;

import br.dubidubibank.entities.Account;
import br.dubidubibank.enums.CommandCode;

public interface SessionService {
  Account getAccount();

  Account getAccountAndCheckPermission(CommandCode commandCode);

  void checkPermission(CommandCode commandCode);

  String getDescription();

  void logIn(int agencyNumber, int accountNumber, String password);

  void logOut();
}
