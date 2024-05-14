package br.dubidubibank.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public record PersonGetRecord(
    @JsonProperty(required = true) String email,
    @JsonProperty(required = true) Long id,
    @JsonProperty(required = true) String name)
    implements Serializable {}
