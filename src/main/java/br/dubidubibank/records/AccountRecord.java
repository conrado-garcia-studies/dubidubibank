package br.dubidubibank.records;

import br.dubidubibank.entities.Command;
import java.io.Serializable;
import java.util.List;

public record AccountRecord(
    Integer accountNumber,
    Double balance,
    List<Command> commands,
    Boolean enabled,
    Long id,
    Integer number,
    PersonRecord person,
    List<RestrictionRecord> restriction,
    String type,
    String zoneId)
    implements Serializable {}
