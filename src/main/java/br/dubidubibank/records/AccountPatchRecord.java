package br.dubidubibank.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AccountPatchRecord(@JsonProperty(required = true) Double balance)
    implements Serializable {}
