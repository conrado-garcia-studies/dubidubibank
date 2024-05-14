package br.dubidubibank.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public record IdRecord(@JsonProperty(required = true) Long id) implements Serializable {}
