package br.dubidubibank.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RestrictionPatchRecord(@JsonProperty(required = true) Double amount)
    implements Serializable {}
