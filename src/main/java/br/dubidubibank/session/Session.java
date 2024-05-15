package br.dubidubibank.session;

import br.dubidubibank.entities.Account;
import br.dubidubibank.entities.Descripted;
import br.dubidubibank.enums.AccountTypeCode;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.*;

@Data
public class Session implements Descripted, Serializable {
  @NonNull
  @Setter(AccessLevel.NONE)
  private Account account;

  @Getter(AccessLevel.NONE)
  @NonNull
  @Setter(AccessLevel.NONE)
  private Instant creationInstant;

  public Session(Account account) {
    this.account = account;
    this.creationInstant = Instant.now();
  }

  @Override
  public String getDescription() {
    if (getAccount().getType().getCode() == AccountTypeCode.ANONYMOUS) {
      return getAccount().getDescription();
    }
    ZonedDateTime creationDateTime = ZonedDateTime.ofInstant(creationInstant, account.getZoneId());
    return String.format(
        "%s%nLog in time: %s",
        getAccount().getDescription(),
        DateTimeFormatter.RFC_1123_DATE_TIME.format(creationDateTime));
  }
}
