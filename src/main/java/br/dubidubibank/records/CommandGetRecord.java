package br.dubidubibank.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public record CommandGetRecord(
    @JsonProperty(required = true) String code,
    @JsonProperty(required = true) Long id,
    @JsonProperty(required = true) String name,
    @JsonProperty(required = true) Boolean restrictable,
    @JsonProperty(required = true) Boolean terminal)
    implements Serializable {}
