package br.dubidubibank.entities;

import br.dubidubibank.enums.CommandCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true)
public class Command extends AbstractEntity implements DescriptedEntity, GrantedAuthority {
  @NonNull
  @NotNull
  @Size(max = 255)
  private String cliBeanCode;

  @NonNull
  @NotNull
  @Size(max = 255)
  private String cliInput;

  @NonNull
  @NotNull
  @Size(max = 255)
  private String cliInputDescription;

  @Column(unique = true)
  @Enumerated(EnumType.STRING)
  @EqualsAndHashCode.Include
  @NonNull
  @NotNull
  private CommandCode code;

  @NonNull
  @NotNull
  @Size(max = 255)
  private String name;

  @NonNull @NotNull private Boolean restrictable;
  @NonNull @NotNull private Boolean terminal;

  @Override
  public String getAuthority() {
    return String.format("ROLE_%s", getCode().name());
  }

  @Override
  public String getDescription() {
    return getName();
  }
}
