package br.dubidubibank.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ExportException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1L;

  public ExportException(String message) {
    super(message);
  }
}
