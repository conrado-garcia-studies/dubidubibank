package br.dubidubibank.records;

import br.dubidubibank.entities.Account;
import br.dubidubibank.entities.Command;
import java.io.Serializable;
import java.time.LocalTime;

public record RestrictionRecord(
    Account account,
    Double amount,
    Command command,
    LocalTime endTime,
    Long id,
    LocalTime startTime)
    implements Serializable {}
