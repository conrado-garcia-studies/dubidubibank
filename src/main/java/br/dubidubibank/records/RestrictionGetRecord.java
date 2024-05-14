package br.dubidubibank.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public record RestrictionGetRecord(
    @JsonProperty(required = true) IdRecord account,
    @JsonProperty(required = true) Double amount,
    @JsonProperty(required = true) IdRecord command,
    @JsonProperty(required = true) Long id,
    @JsonProperty(required = true) String endTime,
    @JsonProperty(required = true) String startTime)
    implements Serializable {}
