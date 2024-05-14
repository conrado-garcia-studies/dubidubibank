package br.dubidubibank.records;

import java.io.Serializable;

public record CommandRecord(String code, String name, Boolean restrictable, Boolean terminal)
    implements Serializable {}
