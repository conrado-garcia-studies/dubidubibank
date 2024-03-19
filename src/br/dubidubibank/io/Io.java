package br.dubidubibank.io;

import java.util.Scanner;

/**
 * Interacts with the user, printing questions, getting inputs from the user and printing results.
 */
public interface Io {
    /**
     * Interacts with the user, printing questions, getting inputs from the user by using a scanner and printing results.
     *
     * @param scanner The scanner
     */
    void execute(Scanner scanner);
}
