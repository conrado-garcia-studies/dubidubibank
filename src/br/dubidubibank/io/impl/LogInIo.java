package br.dubidubibank.io.impl;

import br.dubidubibank.enums.AccountTypeCode;
import br.dubidubibank.services.SessionService;
import br.dubidubibank.session.Session;

import java.util.Scanner;

public class LogInIo extends ScreenTemplateIo {
    public LogInIo(SessionService sessionService) {
        super(sessionService);
    }

    @Override
    protected void executeInternal(Scanner scanner) {
        System.out.print("Agency: ");
        int agencyNumber = scanner.nextInt();
        System.out.print("Account: ");
        int accountNumber = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        sessionService.logIn(agencyNumber, accountNumber, password);
        Session session = sessionService.getSession();
        if (session.getAccount().getType().getCode() == AccountTypeCode.ANONYMOUS) {
            System.out.println("Sorry, it was not possible to log in. Maybe you typed something wrong? Please try" //
                    + " again.");
        } else {
            System.out.println("You are logged in!");
        }
    }
}
