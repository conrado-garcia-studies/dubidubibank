package br.dubidubibank.resources;

import br.dubidubibank.entities.Account;
import br.dubidubibank.entities.Restriction;
import br.dubidubibank.enums.CommandCode;
import br.dubidubibank.mappers.RestrictionGetMapper;
import br.dubidubibank.mappers.RestrictionPatchMapper;
import br.dubidubibank.mappers.RestrictionPostMapper;
import br.dubidubibank.records.ErrorRecord;
import br.dubidubibank.records.RestrictionGetRecord;
import br.dubidubibank.records.RestrictionPatchRecord;
import br.dubidubibank.records.RestrictionPostRecord;
import br.dubidubibank.services.RestrictionService;
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
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Tag(
    name = "Restrictions",
    description =
        "Provides methods used for CRUD of restrictions and for account opening functionality.")
public class RestrictionResource {
  @NonNull private RestrictionGetMapper restrictionGetMapper;
  @NonNull private RestrictionPatchMapper restrictionPatchMapper;
  @NonNull private RestrictionPostMapper restrictionPostMapper;
  @NonNull private RestrictionService restrictionService;
  @NonNull private SessionService sessionService;

  @ApiResponses(
      value = {
        @ApiResponse(
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RestrictionGetRecord.class)),
            description = "Created the restriction.",
            responseCode = "200"),
        @ApiResponse(
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorRecord.class)),
            description =
                "Invalid account ID (it should be \"current\" or a long number), inconsistencies between account ID in"
                    + " parameter and body, overlapping with existing restrictions or other invalid data.",
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
  @Operation(summary = "Creates a restriction of an account.")
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/{accountId}/restrictions")
  @PreAuthorize("hasAnyRole('ROLE_OPEN', 'ROLE_RESTRICT')")
  public ResponseEntity<RestrictionGetRecord> create(
      @Parameter(
              description =
                  "The ID of the account of the restriction to be created. It must be either \"current\" or a long"
                      + " number. Only accounts with access to the Open functionality can create restrictions in"
                      + " accounts that are not the current.")
          @PathVariable
          String accountId,
      @RequestBody(
              content = @Content(schema = @Schema(implementation = RestrictionPostRecord.class)),
              description =
                  "The restriction to create. Please use \"HH:mm:ss.SSS\" format for endTime and startTime (e.g."
                      + " 15:00:59.999 and 14:00:00.000).",
              required = true)
          @org.springframework.web.bind.annotation.RequestBody
          RestrictionPostRecord restrictionRecord) {
    Restriction restriction = restrictionPostMapper.toEntity(restrictionRecord);
    Optional<Long> accountIdAsLong;
    if (accountId.equals("current")) {
      Account account = sessionService.getAccount();
      accountIdAsLong = account.getId();
    } else {
      if (!StringUtils.isNumeric(accountId)) {
        throw new IllegalArgumentException("The ID should be either \"current\" or a long number.");
      }
      accountIdAsLong = Optional.of(Long.valueOf(accountId));
    }
    if (!restriction.getAccount().getId().equals(accountIdAsLong)) {
      throw new IllegalArgumentException(
          "The account ID from the body is not consistent with the account ID in the parameter.");
    }
    if (!sessionService.getAccount().getId().equals(accountIdAsLong)) {
      sessionService.checkPermission(CommandCode.OPEN);
    }
    restriction = restrictionService.save(restriction);
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(restriction.getId())
            .toUri();
    RestrictionGetRecord createdRestrictionRecord = restrictionGetMapper.toRecord(restriction);
    return ResponseEntity.created(uri).body(createdRestrictionRecord);
  }

  @ApiResponses(
      value = {
        @ApiResponse(
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RestrictionGetRecord.class)),
            description = "Deleted the restriction.",
            responseCode = "204"),
        @ApiResponse(
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorRecord.class)),
            description =
                "Forbidden for the current account or when trying to delete a restriction of another account.",
            responseCode = "403"),
        @ApiResponse(
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorRecord.class)),
            description = "Restriction not found.",
            responseCode = "404")
      })
  @DeleteMapping("/current/restrictions/{id}")
  @Operation(summary = "Deletes a restriction of the current account.")
  @PreAuthorize("hasRole('ROLE_RESTRICT')")
  public ResponseEntity<Void> delete(
      @Parameter(description = "The ID of the restriction to be deleted.") @PathVariable Long id) {
    Restriction restriction = restrictionService.findById(id);
    if (!restriction.getAccount().equals(sessionService.getAccount())) {
      throw new AccessDeniedException("You cannot delete a restriction that is not yours.");
    }
    restrictionService.delete(restriction);
    return ResponseEntity.noContent().build();
  }

  @ApiResponses(
      value = {
        @ApiResponse(
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RestrictionGetRecord.class)),
            description = "Found the restriction.",
            responseCode = "200"),
        @ApiResponse(
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorRecord.class)),
            description = "Forbidden for the current account.",
            responseCode = "403")
      })
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/current/restrictions")
  @Operation(summary = "Finds all restrictions of the current account.")
  @PreAuthorize("hasRole('ROLE_RESTRICT')")
  public ResponseEntity<List<RestrictionGetRecord>> findAll() {
    Account account = sessionService.getAccount();
    List<Restriction> restrictions = restrictionService.findByAccount(account);
    List<RestrictionGetRecord> restrictionRecords = restrictionGetMapper.toRecord(restrictions);
    return ResponseEntity.ok().body(restrictionRecords);
  }

  @ApiResponses(
      value = {
        @ApiResponse(
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RestrictionGetRecord.class)),
            description = "Found the restriction.",
            responseCode = "200"),
        @ApiResponse(
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorRecord.class)),
            description =
                "Forbidden for the current account or when trying to find a restriction of another account.",
            responseCode = "403"),
        @ApiResponse(
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorRecord.class)),
            description = "Restriction not found.",
            responseCode = "404")
      })
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/current/restrictions/{id}")
  @Operation(summary = "Finds a restriction by ID.")
  @PreAuthorize("hasRole('ROLE_RESTRICT')")
  public ResponseEntity<RestrictionGetRecord> findById(
      @Parameter(description = "The ID of the restriction to be found.") @PathVariable Long id) {
    Restriction restriction = restrictionService.findById(id);
    if (!restriction.getAccount().equals(sessionService.getAccount())) {
      throw new AccessDeniedException("You cannot get a restriction that is not yours.");
    }
    RestrictionGetRecord restrictionRecord = restrictionGetMapper.toRecord(restriction);
    return ResponseEntity.ok().body(restrictionRecord);
  }

  @ApiResponses(
      value = {
        @ApiResponse(
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RestrictionGetRecord.class)),
            description = "Patched the restriction.",
            responseCode = "200"),
        @ApiResponse(
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorRecord.class)),
            description =
                "Forbidden for the current account or when trying to patch a restriction of another account.",
            responseCode = "403"),
        @ApiResponse(
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorRecord.class)),
            description = "Restriction not found.",
            responseCode = "404")
      })
  @Operation(
      summary =
          "Patches a restriction of the current account by ID. Currently only the amount can be changed.")
  @PatchMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/current/restrictions/{id}")
  @PreAuthorize("hasAnyRole('ROLE_RESTRICT')")
  public ResponseEntity<RestrictionGetRecord> patchById(
      @Parameter(description = "The ID of the restriction to be patched.") @PathVariable Long id,
      @RequestBody(
              content = @Content(schema = @Schema(implementation = RestrictionPatchRecord.class)),
              description = "The restriction to patch.",
              required = true)
          @org.springframework.web.bind.annotation.RequestBody
          RestrictionPatchRecord patchRecord) {
    Restriction restriction = restrictionService.findById(id);
    if (!restriction.getAccount().equals(sessionService.getAccount())) {
      throw new AccessDeniedException("You cannot patch a restriction that is not yours.");
    }
    Restriction patch = restrictionPatchMapper.toEntity(patchRecord);
    restriction = restrictionService.patchById(id, patch);
    RestrictionGetRecord restrictionRecord = restrictionGetMapper.toRecord(restriction);
    return ResponseEntity.ok().body(restrictionRecord);
  }
}
