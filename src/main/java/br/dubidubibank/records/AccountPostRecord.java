package br.dubidubibank.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AccountPostRecord(
    @JsonProperty(required = true) Integer agencyNumber,
    @JsonProperty(required = true) Double balance,
    @JsonProperty(required = true) Boolean enabled,
    @JsonProperty(required = true) Integer number,
    @JsonProperty(required = true) String password,
    @JsonProperty(required = true) PersonPostRecord person,
    @JsonProperty(required = true) String type,
    @JsonProperty(required = true) String zoneId)
    implements Serializable {}
