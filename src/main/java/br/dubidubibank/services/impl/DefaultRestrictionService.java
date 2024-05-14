package br.dubidubibank.services.impl;

import br.dubidubibank.entities.Account;
import br.dubidubibank.entities.Command;
import br.dubidubibank.entities.Restriction;
import br.dubidubibank.exceptions.ResourceNotFoundException;
import br.dubidubibank.repositories.RestrictionRepository;
import br.dubidubibank.services.RestrictionService;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultRestrictionService implements RestrictionService {
  @NonNull private RestrictionRepository repository;

  @Override
  public Optional<Restriction> findActiveByAccountAndCommand(Account account, Command command) {
    LocalTime now = ZonedDateTime.now(account.getZoneId()).toLocalTime();
    return repository.findByAccountAndCommandAndEndTimeGreaterThanEqualAndStartTimeLessThanEqual(
        account, command, now, now);
  }

  @Override
  public List<Restriction> findByAccount(Account account) {
    return repository.findByAccount(account);
  }

  @Override
  public Restriction findById(Long id) {
    return repository
        .findById(id)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    String.format("Could not find account by identifier %s.", id)));
  }

  @Override
  public void delete(Restriction restriction) {
    repository.delete(restriction);
  }

  @Override
  public Restriction patchById(Long id, Restriction patch) {
    Restriction restriction = repository.getReferenceById(id);
    Double amount = patch.getAmount();
    restriction.setAmount(amount);
    return repository.save(restriction);
  }

  @Override
  public Restriction save(Restriction restriction) {
    return repository.save(restriction);
  }
}
