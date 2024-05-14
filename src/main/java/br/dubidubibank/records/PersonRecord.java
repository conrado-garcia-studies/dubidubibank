package br.dubidubibank.records;

import java.io.Serializable;

public record PersonRecord(String email, String name) implements Serializable {}
