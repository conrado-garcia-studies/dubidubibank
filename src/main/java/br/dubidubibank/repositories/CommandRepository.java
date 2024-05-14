package br.dubidubibank.repositories;

import br.dubidubibank.entities.Command;
import br.dubidubibank.enums.CommandCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandRepository extends JpaRepository<Command, Long> {
  Optional<Command> findByCode(CommandCode code);
}
