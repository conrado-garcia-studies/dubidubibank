package br.dubidubibank.records;

import java.io.Serializable;
import java.time.Instant;

public record ErrorRecord(
    String error, Instant instant, String message, String path, Integer statusCode)
    implements Serializable {}
