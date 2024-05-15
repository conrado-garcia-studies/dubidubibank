package br.dubidubibank.repositories;

import br.dubidubibank.entities.Account;
import br.dubidubibank.entities.AccountType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
  Optional<Account> findByAgencyNumberAndNumber(int agencyNumber, int number);

  Optional<Account> findByType(AccountType type);
}
