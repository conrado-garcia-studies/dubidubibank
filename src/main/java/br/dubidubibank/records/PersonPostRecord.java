package br.dubidubibank.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PersonPostRecord(
    @JsonProperty(required = true) String email, @JsonProperty(required = true) String name)
    implements Serializable {}
