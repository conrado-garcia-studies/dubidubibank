package br.dubidubibank.resources;

import br.dubidubibank.entities.Account;
import br.dubidubibank.records.ExportRecord;
import br.dubidubibank.services.SessionService;
import br.dubidubibank.services.TransactionExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.AbstractResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("accounts/current/exports")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "Exports", description = "Enables the user to generate and download reports.")
public class ExportResource {
  @NonNull private SessionService sessionService;
  @NonNull private TransactionExportService transactionExportService;

  @ApiResponses(
      value = {
        @ApiResponse(
            content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE),
            description =
                "Report exported. If access is denied, the report is generated with an error message",
            responseCode = "200"),
        @ApiResponse(
            description = "It was not possible to export the report.",
            responseCode = "500")
      })
  @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, value = "/transaction")
  @Operation(summary = "Exports a report in CSV format containing information about transactions.")
  public ResponseEntity<AbstractResource> exportTransaction() {
    HttpHeaders headers = new HttpHeaders();
    Account account = sessionService.getAccount();
    ExportRecord export = transactionExportService.getExportAndDeleteFile(account);
    headers.add(
        HttpHeaders.CONTENT_DISPOSITION,
        String.format("attachment; filename=%s", export.path().getFileName()));
    return ResponseEntity.ok()
        .headers(headers)
        .contentLength(export.resource().contentLength())
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(export.resource());
  }
}
