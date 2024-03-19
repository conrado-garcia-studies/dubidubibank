package br.dubidubibank.application;

import br.dubidubibank.daos.*;
import br.dubidubibank.daos.impl.*;
import br.dubidubibank.enums.CommandCode;
import br.dubidubibank.io.Io;
import br.dubidubibank.io.impl.*;
import br.dubidubibank.persistence.InMemoryDatabase;
import br.dubidubibank.persistence.impl.DefaultInMemoryDatabase;
import br.dubidubibank.services.SessionService;
import br.dubidubibank.services.TransactionExportService;
import br.dubidubibank.services.impl.DefaultSessionService;
import br.dubidubibank.services.impl.DefaultTransactionExportService;

import java.util.HashMap;
import java.util.Map;

public class AppConfig {
    private static AppConfig instance;
    private AccountDao accountDao;
    private AccountTypeDao accountTypeDao;
    private CommandDao commandDao;
    private Map<CommandCode, Io> commandCodeToIo;
    private Io commandIo;
    private Io depositIo;
    private Io exportIo;
    private InMemoryDatabase inMemoryDatabase;
    private LimitDao limitDao;
    private Io logInIo;
    private Io logOutIo;
    private Io openIo;
    private Io quitIo;
    private Io restrictIo;
    private SessionService sessionService;
    private TransactionDao transactionDao;
    private TransactionExportService transactionExportService;
    private Io transferIo;
    private UserDao userDao;
    private Io withdrawIo;

    public Io commandIo() {
        if (commandIo == null) {
            commandIo = new CommandIo(commandCodeToIo(), sessionService());
        }
        return commandIo;
    }

    public Map<CommandCode, Io> commandCodeToIo() {
        if (commandCodeToIo == null) {
            commandCodeToIo = new HashMap<>();
            commandCodeToIo.put(CommandCode.DEPOSIT, depositIo());
            commandCodeToIo.put(CommandCode.EXPORT, exportIo());
            commandCodeToIo.put(CommandCode.LOG_IN, logInIo());
            commandCodeToIo.put(CommandCode.LOG_OUT, logOutIo());
            commandCodeToIo.put(CommandCode.OPEN, openIo());
            commandCodeToIo.put(CommandCode.QUIT, quitIo());
            commandCodeToIo.put(CommandCode.RESTRICT, restrictIo());
            commandCodeToIo.put(CommandCode.TRANSFER, transferIo());
            commandCodeToIo.put(CommandCode.WITHDRAW, withdrawIo());
        }
        return commandCodeToIo;
    }

    public Io depositIo() {
        if (depositIo == null) {
            depositIo = new DepositIo(commandDao(), sessionService(), transactionDao());
        }
        return depositIo;
    }

    public Io exportIo() {
        if (exportIo == null) {
            exportIo = new ExportIo(sessionService(), transactionExportService());
        }
        return exportIo;
    }

    public TransactionExportService transactionExportService() {
        if (transactionExportService == null) {
            transactionExportService = new DefaultTransactionExportService(transactionDao());
        }
        return transactionExportService;
    }

    public Io logInIo() {
        if (logInIo == null) {
            logInIo = new LogInIo(sessionService());
        }
        return logInIo;
    }

    public Io logOutIo() {
        if (logOutIo == null) {
            logOutIo = new LogOutIo(sessionService());
        }
        return logOutIo;
    }

    public Io openIo() {
        if (openIo == null) {
            openIo = new OpenIo(accountDao(), accountTypeDao(), limitDao(), sessionService(), userDao());
        }
        return openIo;
    }

    public AccountTypeDao accountTypeDao() {
        if (accountTypeDao == null) {
            accountTypeDao = new InMemoryAccountTypeDao(inMemoryDatabase());
        }
        return accountTypeDao;
    }

    public UserDao userDao() {
        if (userDao == null) {
            userDao = new InMemoryUserDao(inMemoryDatabase());
        }
        return userDao;
    }

    public Io quitIo() {
        if (quitIo == null) {
            quitIo = new QuitIo(sessionService());
        }
        return quitIo;
    }

    public Io restrictIo() {
        if (restrictIo == null) {
            restrictIo = new RestrictIo(accountDao(), limitDao(), sessionService());
        }
        return restrictIo;
    }

    public LimitDao limitDao() {
        if (limitDao == null) {
            limitDao = new InMemoryLimitDao(inMemoryDatabase());
        }
        return limitDao;
    }

    public Io transferIo() {
        if (transferIo == null) {
            transferIo = new TransferIo(accountDao(), commandDao(), sessionService(), transactionDao());
        }
        return transferIo;
    }

    public Io withdrawIo() {
        if (withdrawIo == null) {
            withdrawIo = new WithdrawIo(commandDao(), sessionService(), transactionDao());
        }
        return withdrawIo;
    }

    public CommandDao commandDao() {
        if (commandDao == null) {
            commandDao = new InMemoryCommandDao(inMemoryDatabase());
        }
        return commandDao;
    }

    public SessionService sessionService() {
        if (sessionService == null) {
            sessionService = new DefaultSessionService(accountDao());
        }
        return sessionService;
    }

    public AccountDao accountDao() {
        if (accountDao == null) {
            accountDao = new InMemoryAccountDao(inMemoryDatabase());
        }
        return accountDao;
    }

    public TransactionDao transactionDao() {
        if (transactionDao == null) {
            transactionDao = new InMemoryTransactionDao(inMemoryDatabase());
        }
        return transactionDao;
    }

    public InMemoryDatabase inMemoryDatabase() {
        if (inMemoryDatabase == null) {
            inMemoryDatabase = new DefaultInMemoryDatabase();
        }
        return inMemoryDatabase;
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }
}
