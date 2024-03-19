package br.dubidubibank.io.impl;

import br.dubidubibank.io.Io;
import br.dubidubibank.services.SessionService;
import br.dubidubibank.session.Session;
import br.dubidubibank.utils.IoUtils;

import java.util.Scanner;

public abstract class ScreenTemplateIo implements Io {
    protected SessionService sessionService;

    public ScreenTemplateIo() {
    }

    public ScreenTemplateIo(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void execute(Scanner scanner) {
        beforeExecute(scanner);
        executeInternal(scanner);
        afterExecute(scanner);
    }

    protected void beforeExecute(Scanner scanner) {
        IoUtils.clearScreen();
        System.out.println("Dubidubibank");
        Session session = sessionService.getSession();
        System.out.println(session);
        System.out.println();
    }

    protected abstract void executeInternal(Scanner scanner);

    protected void afterExecute(Scanner scanner) {
        scanner.nextLine();
    }
}
