package br.dubidubibank.services.impl;

import br.dubidubibank.entities.Command;
import br.dubidubibank.enums.CommandCode;
import br.dubidubibank.exceptions.ResourceNotFoundException;
import br.dubidubibank.repositories.CommandRepository;
import br.dubidubibank.services.CommandService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultCommandService implements CommandService {
  @NonNull private CommandRepository repository;

  @Override
  public Command findByCode(CommandCode code) {
    return repository
        .findByCode(code)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    String.format("Could not find command by code %s.", code)));
  }

  @Override
  public Command findById(Long id) {
    return repository
        .findById(id)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    String.format("Could not find command by identifier %s.", id)));
  }
}
