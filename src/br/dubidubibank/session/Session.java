package br.dubidubibank.session;

import br.dubidubibank.entities.Account;
import br.dubidubibank.enums.AccountTypeCode;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Session implements Serializable {
    private final Account account;
    private final ZonedDateTime creationTime;

    public Session(Account account) {
        this.account = account;
        this.creationTime = ZonedDateTime.now();
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public String toString() {
        if (account.getType().getCode() == AccountTypeCode.ANONYMOUS) {
            return account.toString();
        }
        return String.format("%s%nLog in time: %s", account, DateTimeFormatter.RFC_1123_DATE_TIME.format(creationTime));
    }
}
