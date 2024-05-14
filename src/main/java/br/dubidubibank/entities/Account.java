package br.dubidubibank.entities;

import br.dubidubibank.enums.AccountTypeCode;
import br.dubidubibank.validation.constraints.ValidNumbers;
import br.dubidubibank.validation.validators.NotOverlappingAccountRestrictionsValidator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Entity
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@RequiredArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"agency_number", "number"}))
@ToString(callSuper = true)
@ValidNumbers
public class Account extends AbstractEntity implements DescriptedEntity, UserDetails {
  @EqualsAndHashCode.Include @NonNull @NotNull private Integer agencyNumber;

  @NonNull @NotNull private Double balance;

  @NonNull @NotNull private Boolean enabled;

  @Getter(AccessLevel.NONE)
  Instant lastLogInInstant;

  @EqualsAndHashCode.Include @NonNull @NotNull private Integer number;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @NotEmpty
  @NonNull
  @NotNull
  @Size(max = 255)
  @ToString.Exclude
  private String password;

  @JoinColumn(name = "person_id")
  @ManyToOne
  @NonNull
  @NotNull
  private Person person;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "account", orphanRemoval = true)
  @Setter(AccessLevel.NONE)
  @ToString.Exclude
  private List<Restriction> restrictions = new ArrayList<>();

  @JoinColumn(name = "type_id")
  @ManyToOne
  @NonNull
  @NotNull
  private AccountType type;

  @NonNull @NotNull private ZoneId zoneId;

  public void addRestriction(Restriction restriction) {
    NotOverlappingAccountRestrictionsValidator validator =
        new NotOverlappingAccountRestrictionsValidator();
    if (!validator.isValid(restriction, null)) {
      throw new IllegalArgumentException(
          "The new restriction overlaps an existing restriction in the account. Please try other range.\n");
    }
    restrictions.add(restriction);
  }

  public void deposit(double amount) {
    balance += amount;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return getCommands();
  }

  public List<Command> getCommands() {
    return getType().getCommands();
  }

  @Override
  public String getDescription() {
    if (getType().getCode() == AccountTypeCode.ANONYMOUS) {
      return String.format("User: %s", getType().getDescription());
    }
    return String.format(
        "User: %s%nAgency: %s%nAccount: %s%nType: %s%nBalance: $%.2f",
        getPerson().getDescription(),
        getAgencyNumber(),
        getNumber(),
        getType().getDescription(),
        getBalance());
  }

  public Optional<Instant> getLastLogInInstant() {
    return Optional.ofNullable(lastLogInInstant);
  }

  @Override
  public String getUsername() {
    return String.format("%d.%d", getAgencyNumber(), getNumber());
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  public void removeRestriction(Restriction restriction) {
    restrictions.remove(restriction);
  }

  public void withdraw(double amount) {
    balance -= amount;
  }
}
