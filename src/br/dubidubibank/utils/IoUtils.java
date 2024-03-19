package br.dubidubibank.utils;

import br.dubidubibank.entities.AccountType;
import br.dubidubibank.entities.Command;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IoUtils {

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static AccountType getAccountType(Collection<AccountType> accountTypes, String input) {
        return accountTypes //
                .stream() //
                .filter(accountType -> accountType.getInput().isPresent() //
                        && IoUtils.isActualInputMatchesExpectedInput(accountType.getInput().get(), input)) //
                .findFirst() //
                .orElseThrow(() -> new IllegalArgumentException("The account type you typed is invalid. Please try" //
                        + " again."));
    }

    public static String getAccountTypeInputDescriptions(Collection<AccountType> accountTypes) {
        return accountTypes //
                .stream() //
                .map(AccountType::getInputDescription) //
                .filter(Optional::isPresent) //
                .map(Optional::get) //
                .collect(Collectors.joining(", "));
    }

    public static Command getCommand(List<Command> commands, String input) {
        return commands //
                .stream() //
                .filter(command -> IoUtils.isActualInputMatchesExpectedInput(command.getInput(), input)) //
                .findFirst() //
                .orElseThrow(() -> new IllegalArgumentException("The command you typed is invalid. Please try again."));
    }

    public static String getCommandInputDescriptions(Collection<Command> commands) {
        return commands //
                .stream() //
                .map(Command::getInputDescription) //
                .collect(Collectors.joining(", "));
    }

    public static String getIndexSelectionInputDescriptions(List<?> objects) {
        return IntStream.range(0, objects.size()) //
                .mapToObj(i -> String.format("[%s] %s", i + 1, objects.get(i))) //
                .collect(Collectors.joining(", "));
    }

    public static int getSelectedIndex(List<?> objects, String input) {
        return IntStream.range(0, objects.size()) //
                .mapToObj(i -> String.format("%s", i + 1)) //
                .filter(expectedInput -> isActualInputMatchesExpectedInput(expectedInput, input)) //
                .map(expectedInput -> Integer.parseInt(expectedInput) - 1) //
                .findFirst() //
                .orElseThrow(() -> new IllegalArgumentException("The option you typed is invalid. Please try again."));
    }

    public static boolean isActualInputMatchesExpectedInput(String expectedInput, String actualInput) {
        if (expectedInput == null && actualInput == null) {
            return true;
        }
        if (expectedInput == null || actualInput == null) {
            return false;
        }
        return actualInput.toUpperCase().startsWith(expectedInput.toUpperCase());
    }
}
