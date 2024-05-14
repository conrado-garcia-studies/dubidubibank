package br.dubidubibank.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RestrictionPostRecord(
    @JsonProperty(required = true) IdRecord account,
    @JsonProperty(required = true) Double amount,
    @JsonProperty(required = true) IdRecord command,
    @JsonProperty(required = true) String endTime,
    @JsonProperty(required = true) String startTime)
    implements Serializable {}
