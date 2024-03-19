package br.dubidubibank.io.impl;

import br.dubidubibank.services.SessionService;

import java.util.Scanner;

public class LogOutIo extends ScreenTemplateIo {
    public LogOutIo(SessionService sessionService) {
        super(sessionService);
    }

    @Override
    protected void executeInternal(Scanner scanner) {
        sessionService.logOut();
        System.out.println("You are logged out!");
    }
}
