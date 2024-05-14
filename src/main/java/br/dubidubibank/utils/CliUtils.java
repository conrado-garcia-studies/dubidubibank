package br.dubidubibank.utils;

import br.dubidubibank.entities.AccountType;
import br.dubidubibank.entities.Command;
import br.dubidubibank.entities.DescriptedEntity;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public interface CliUtils {
  AccountType findAccountType(Collection<AccountType> accountTypes, String input);

  String findAccountTypeInputDescriptions(Collection<AccountType> accountTypes);

  Command findCommand(List<Command> commands, String input);

  String findCommandInputDescriptions(Collection<Command> commands);

  String findIndexSelectionInputDescriptions(List<? extends DescriptedEntity> entities);

  List<Command> findRestrictableCommands(Collection<Command> commands);

  int findSelectedIndex(List<?> objects, String input);

  boolean isActualInputMatchesExpectedInput(String expectedInput, String actualInput);

  LocalTime toEndLocalTime(String timestamp);

  LocalTime toStartLocalTime(String timestamp);
}
