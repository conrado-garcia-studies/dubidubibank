package br.dubidubibank.io.impl;

import br.dubidubibank.services.SessionService;

import java.util.Scanner;

public class QuitIo extends ScreenTemplateIo {
    public QuitIo(SessionService sessionService) {
        super(sessionService);
    }

    @Override
    protected void executeInternal(Scanner scanner) {
        System.out.println("Thanks for using Dubidubibank!");
    }
}
