package br.dubidubibank.services;

import br.dubidubibank.entities.Command;
import br.dubidubibank.enums.CommandCode;

public interface CommandService {
  Command findByCode(CommandCode code);

  Command findById(Long id);
}
