package br.dubidubibank.records;

import java.nio.file.Path;
import org.springframework.core.io.ByteArrayResource;

public record ExportRecord(Path path, ByteArrayResource resource) {}
