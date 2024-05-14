package br.dubidubibank.repositories;

import br.dubidubibank.entities.AccountType;
import br.dubidubibank.enums.AccountTypeCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, Long> {
  Optional<AccountType> findByCode(AccountTypeCode code);

  List<AccountType> findByCliInputNotNullAndCliInputDescriptionNotNull();
}
