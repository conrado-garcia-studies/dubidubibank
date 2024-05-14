package br.dubidubibank.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public record AuthenticationSuccessRecord(
    @JsonProperty(required = true) String message, @JsonProperty(required = true) String sessionId)
    implements Serializable {}
