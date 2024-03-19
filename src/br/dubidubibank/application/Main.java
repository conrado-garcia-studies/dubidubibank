package br.dubidubibank.application;

import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner scanner = new Scanner(System.in);
        AppConfig.getInstance().commandIo().execute(scanner);
        scanner.close();
    }
}
