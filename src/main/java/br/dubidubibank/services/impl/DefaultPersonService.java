package br.dubidubibank.services.impl;

import br.dubidubibank.entities.Person;
import br.dubidubibank.repositories.PersonRepository;
import br.dubidubibank.services.PersonService;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultPersonService implements PersonService {
  @NonNull private PersonRepository repository;

  @Override
  public Optional<Person> findOptionalByEmail(String email) {
    return repository.findByEmail(email);
  }

  @Override
  public Person save(Person person) {
    return repository.save(person);
  }
}
