package br.dubidubibank.services;

import br.dubidubibank.entities.AccountType;
import br.dubidubibank.enums.AccountTypeCode;
import java.util.List;

public interface AccountTypeService {
  AccountType findByCode(AccountTypeCode code);

  List<AccountType> findSelectable();
}
