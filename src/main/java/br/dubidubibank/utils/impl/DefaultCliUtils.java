package br.dubidubibank.utils.impl;

import br.dubidubibank.entities.AccountType;
import br.dubidubibank.entities.Command;
import br.dubidubibank.entities.DescriptedEntity;
import br.dubidubibank.utils.CliUtils;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DefaultCliUtils implements CliUtils {
  @Override
  public AccountType findAccountType(Collection<AccountType> accountTypes, String input) {
    return accountTypes //
        .stream() //
        .filter(
            accountType ->
                accountType.getCliInput().isPresent() //
                    && isActualInputMatchesExpectedInput(
                        accountType.getCliInput().get(), input)) //
        .findFirst() //
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "The account type you typed is invalid. Please try" //
                        + " again."));
  }

  @Override
  public String findAccountTypeInputDescriptions(Collection<AccountType> accountTypes) {
    return accountTypes //
        .stream() //
        .map(AccountType::getCliInputDescription) //
        .filter(Optional::isPresent) //
        .map(Optional::get) //
        .collect(Collectors.joining(", "));
  }

  @Override
  public Command findCommand(List<Command> commands, String input) {
    return commands //
        .stream() //
        .filter(command -> isActualInputMatchesExpectedInput(command.getCliInput(), input)) //
        .findFirst() //
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "The command you typed is invalid. Please try again."));
  }

  @Override
  public String findCommandInputDescriptions(Collection<Command> commands) {
    return commands //
        .stream() //
        .map(Command::getCliInputDescription) //
        .collect(Collectors.joining(", "));
  }

  @Override
  public String findIndexSelectionInputDescriptions(List<? extends DescriptedEntity> entities) {
    return IntStream.range(0, entities.size()) //
        .mapToObj(i -> String.format("[%d] %s", i + 1, entities.get(i).getDescription())) //
        .collect(Collectors.joining(", "));
  }

  @Override
  public List<Command> findRestrictableCommands(Collection<Command> commands) {
    return commands.stream() //
        .filter(Command::getRestrictable) //
        .sorted(Comparator.comparing(Command::getCliInputDescription)) //
        .toList();
  }

  @Override
  public int findSelectedIndex(List<?> objects, String input) {
    return IntStream.range(0, objects.size()) //
        .mapToObj(i -> String.format("%s", i + 1)) //
        .filter(expectedInput -> isActualInputMatchesExpectedInput(expectedInput, input)) //
        .map(expectedInput -> Integer.parseInt(expectedInput) - 1) //
        .findFirst() //
        .orElseThrow(
            () ->
                new IllegalArgumentException("The option you typed is invalid. Please try again."));
  }

  @Override
  public boolean isActualInputMatchesExpectedInput(String expectedInput, String actualInput) {
    if (expectedInput == null && actualInput == null) {
      return true;
    }
    if (expectedInput == null || actualInput == null) {
      return false;
    }
    return actualInput.toUpperCase().startsWith(expectedInput.toUpperCase());
  }

  @Override
  public LocalTime toEndLocalTime(String timestamp) {
    return toStartLocalTime(timestamp) //
        .withSecond(59) //
        .withNano(999_000_000);
  }

  @Override
  public LocalTime toStartLocalTime(String timestamp) {
    try {
      String[] hourAndMinute = timestamp.split(":");
      return LocalTime.of(Integer.parseInt(hourAndMinute[0]), Integer.parseInt(hourAndMinute[1]));
    } catch (ArrayIndexOutOfBoundsException | DateTimeException | NumberFormatException e) {
      throw new IllegalArgumentException("The format of the timestamp you typed is wrong.");
    }
  }
}
