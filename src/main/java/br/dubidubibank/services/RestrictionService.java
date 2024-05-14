package br.dubidubibank.services;

import br.dubidubibank.entities.Account;
import br.dubidubibank.entities.Command;
import br.dubidubibank.entities.Restriction;
import java.util.List;
import java.util.Optional;

public interface RestrictionService {
  Optional<Restriction> findActiveByAccountAndCommand(Account account, Command command);

  List<Restriction> findByAccount(Account account);

  Restriction findById(Long id);

  void delete(Restriction restriction);

  Restriction patchById(Long id, Restriction patch);

  Restriction save(Restriction restriction);
}
