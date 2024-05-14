package br.dubidubibank.resources;

import br.dubidubibank.entities.Account;
import br.dubidubibank.enums.CommandCode;
import br.dubidubibank.mappers.AccountGetMapper;
import br.dubidubibank.mappers.AccountPatchMapper;
import br.dubidubibank.mappers.AccountPostMapper;
import br.dubidubibank.records.AccountGetRecord;
import br.dubidubibank.records.AccountPatchRecord;
import br.dubidubibank.records.AccountPostRecord;
import br.dubidubibank.records.ErrorRecord;
import br.dubidubibank.services.AccountService;
import br.dubidubibank.services.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Tag(
    name = "Accounts",
    description =
        "Provides methods used for account opening, deposit, transfer, and withdraw functionalities.")
public class AccountResource {
  @NonNull private AccountGetMapper accountGetMapper;
  @NonNull private AccountPatchMapper accountPatchMapper;
  @NonNull private AccountPostMapper accountPostMapper;
  @NonNull private AccountService accountService;
  @NonNull private SessionService sessionService;

  @ApiResponses(
      value = {
        @ApiResponse(
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AccountGetRecord.class)),
            description = "Created the account.",
            responseCode = "200"),
        @ApiResponse(
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorRecord.class)),
            description = "Invalid data.",
            responseCode = "400"),
        @ApiResponse(
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorRecord.class)),
            description = "Forbidden for the current account.",
            responseCode = "403")
      })
  @Operation(summary = "Creates an account.")
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_OPEN')")
  public ResponseEntity<AccountGetRecord> create(
      @RequestBody(
              content = @Content(schema = @Schema(implementation = AccountPostRecord.class)),
              description =
                  "The account to create. Examples for type are \"ADMIN\", \"CHECKING\" and \"SAVINGS\". An example for"
                      + " zoneId is \"America/Sao_Paulo\". The email must be in a valid email format.",
              required = true)
          @org.springframework.web.bind.annotation.RequestBody
          AccountPostRecord accountRecord) {
    Account account = accountPostMapper.toEntity(accountRecord);
    account = accountService.save(account);
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(account.getId())
            .toUri();
    AccountGetRecord createdAccountRecord = accountGetMapper.toRecord(account);
    return ResponseEntity.created(uri).body(createdAccountRecord);
  }

  @ApiResponses(
      value = {
        @ApiResponse(
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AccountGetRecord.class)),
            description = "Found the account.",
            responseCode = "200"),
        @ApiResponse(
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorRecord.class)),
            description =
                "Invalid ID, which should be either \"current\" or a long number, or other invalid data.",
            responseCode = "400"),
        @ApiResponse(
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorRecord.class)),
            description = "Forbidden for the current account.",
            responseCode = "403"),
        @ApiResponse(
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorRecord.class)),
            description = "Account not found.",
            responseCode = "404")
      })
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Finds an account by ID.")
  public ResponseEntity<AccountGetRecord> findById(
      @Parameter(
              description =
                  "The ID of the account to be found. It must be either \"current\" or a long number. Only accounts"
                      + " with access to the Open functionality can find accounts that are not the current.")
          @PathVariable
          String id) {
    Optional<Long> idAsLong;
    if (id.equals("current")) {
      Account account = sessionService.getAccount();
      idAsLong = account.getId();
    } else {
      if (!StringUtils.isNumeric(id)) {
        throw new IllegalArgumentException("The ID should be either \"current\" or a long number.");
      }
      idAsLong = Optional.of(Long.valueOf(id));
    }
    if (!sessionService.getAccount().getId().equals(idAsLong)) {
      sessionService.checkPermission(CommandCode.OPEN);
    }
    Account account = accountService.findById(idAsLong.orElse(null));
    AccountGetRecord accountRecord = accountGetMapper.toRecord(account);
    return ResponseEntity.ok().body(accountRecord);
  }

  @ApiResponses(
      value = {
        @ApiResponse(
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AccountGetRecord.class)),
            description = "Patched the account.",
            responseCode = "200"),
        @ApiResponse(
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorRecord.class)),
            description =
                "Inconsistent or invalid data (e.g. the amount to deposit, transfer or withdraw is zero or"
                    + " negative).",
            responseCode = "400"),
        @ApiResponse(
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorRecord.class)),
            description = "Forbidden for the current account.",
            responseCode = "403"),
        @ApiResponse(
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorRecord.class)),
            description = "Account not found.",
            responseCode = "404")
      })
  @Operation(
      summary =
          "Patches the current account. It provides deposit, transfer and withdraw functionalities.")
  @PatchMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/current")
  @PreAuthorize("hasAnyRole('ROLE_DEPOSIT', 'ROLE_TRANSFER', 'ROLE_WITHDRAW')")
  public ResponseEntity<AccountGetRecord> patchCurrent(
      @RequestBody(
              content = @Content(schema = @Schema(implementation = AccountPatchRecord.class)),
              description =
                  "The account to patch. It contains only a balance, which represents the balance of the"
                      + " account after the patching. If the balance that comes from the body is greater than the"
                      + " current balance of the account, then a deposit is performed. The deposited amount is equal to"
                      + " the balance of the body minus the current balance. Otherwise it depends on the targetAccount"
                      + " query to decide between transfer and withdraw. If both targetAccount.agencyNumber and"
                      + " targetAccount.number are not null, then a transfer is performed. If both are mull, then a"
                      + " withdraw is performed. The transferred or withdrawn amount is equal to the current balance"
                      + " minus the balance of the body.",
              required = true)
          @org.springframework.web.bind.annotation.RequestBody
          AccountPatchRecord patchRecord,
      @Parameter(description = "The agency number of the target account to transfer to.")
          @RequestParam(name = "targetAccount.agencyNumber", required = false)
          Integer targetAccountAgencyNumber,
      @Parameter(description = "The number of the target account to transfer to.")
          @RequestParam(name = "targetAccount.number", required = false)
          Integer targetAccountNumber) {
    Account account = sessionService.getAccount();
    Account patch = accountPatchMapper.toEntity(patchRecord);
    checkPatchPermission(patch, targetAccountAgencyNumber, targetAccountNumber, account);
    account = accountService.patch(account, patch, targetAccountAgencyNumber, targetAccountNumber);
    AccountGetRecord accountRecord = accountGetMapper.toRecord(account);
    return ResponseEntity.ok().body(accountRecord);
  }

  private void checkPatchPermission(
      Account patch,
      Integer targetAccountAgencyNumber,
      Integer targetAccountNumber,
      Account account) {
    if (targetAccountAgencyNumber != null && targetAccountNumber != null) {
      if (patch.getBalance() < account.getBalance()) {
        sessionService.checkPermission(CommandCode.TRANSFER);
      }
    } else if (patch.getBalance() > account.getBalance()) {
      sessionService.checkPermission(CommandCode.DEPOSIT);
    } else if (patch.getBalance() < account.getBalance()) {
      sessionService.checkPermission(CommandCode.WITHDRAW);
    }
  }
}
