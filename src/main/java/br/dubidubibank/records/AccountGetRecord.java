package br.dubidubibank.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

public record AccountGetRecord(
    @JsonProperty(required = true) Integer agencyNumber,
    @JsonProperty(required = true) Double balance,
    @JsonProperty(required = true) List<CommandGetRecord> commands,
    @JsonProperty(required = true) Boolean enabled,
    @JsonProperty(required = true) Long id,
    @JsonProperty(required = true) Integer number,
    @JsonProperty(required = true) PersonGetRecord person,
    @JsonProperty(required = true) List<RestrictionGetRecord> restrictions,
    @JsonProperty(required = true) String type,
    @JsonProperty(required = true) String zoneId)
    implements Serializable {}
