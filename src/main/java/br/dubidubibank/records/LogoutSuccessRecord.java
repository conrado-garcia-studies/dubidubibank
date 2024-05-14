package br.dubidubibank.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public record LogoutSuccessRecord(@JsonProperty(required = true) String message)
    implements Serializable {}
