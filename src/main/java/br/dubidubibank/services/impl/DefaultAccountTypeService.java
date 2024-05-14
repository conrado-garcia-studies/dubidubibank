package br.dubidubibank.services.impl;

import br.dubidubibank.entities.AccountType;
import br.dubidubibank.enums.AccountTypeCode;
import br.dubidubibank.exceptions.ResourceNotFoundException;
import br.dubidubibank.repositories.AccountTypeRepository;
import br.dubidubibank.services.AccountTypeService;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultAccountTypeService implements AccountTypeService {
  @NonNull private AccountTypeRepository repository;

  @Override
  public AccountType findByCode(AccountTypeCode code) {
    return repository
        .findByCode(code)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    String.format("Could not find account type by code %s.", code)));
  }

  @Override
  public List<AccountType> findSelectable() {
    return repository.findByCliInputNotNullAndCliInputDescriptionNotNull();
  }
}
