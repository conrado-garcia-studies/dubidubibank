package br.dubidubibank.repositories;

import br.dubidubibank.entities.Account;
import br.dubidubibank.entities.Command;
import br.dubidubibank.entities.Restriction;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestrictionRepository extends JpaRepository<Restriction, Long> {
  List<Restriction> findByAccount(Account account);

  Optional<Restriction> findByAccountAndCommandAndEndTimeGreaterThanEqualAndStartTimeLessThanEqual(
      Account account, Command command, LocalTime time1, LocalTime time2);
}
