package br.dubidubibank.services;

import br.dubidubibank.entities.Person;
import java.util.Optional;

public interface PersonService {
  Optional<Person> findOptionalByEmail(String email);

  Person save(Person person);
}
