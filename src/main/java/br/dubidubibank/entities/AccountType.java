package br.dubidubibank.entities;

import br.dubidubibank.enums.AccountTypeCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.*;
import lombok.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true)
public class AccountType extends AbstractEntity implements DescriptedEntity {
  @Getter(AccessLevel.NONE)
  @Size(max = 255)
  private String cliInput;

  @Getter(AccessLevel.NONE)
  @Size(max = 255)
  private String cliInputDescription;

  @Column(unique = true)
  @Enumerated(EnumType.STRING)
  @EqualsAndHashCode.Include
  @NonNull
  @NotNull
  private AccountTypeCode code;

  @JoinTable(
      inverseJoinColumns = @JoinColumn(name = "account_type_id"), //
      joinColumns = @JoinColumn(name = "command_id"), //
      name = "account_type_has_command")
  @ManyToMany(fetch = FetchType.EAGER)
  @NotEmpty
  @Setter(AccessLevel.NONE)
  private List<Command> commands = new ArrayList<>();

  @NonNull
  @NotNull
  @Size(max = 255)
  private String name;

  public void addCommand(Command command) {
    commands.add(command);
  }

  public Optional<String> getCliInput() {
    return Optional.ofNullable(cliInput);
  }

  public Optional<String> getCliInputDescription() {
    return Optional.ofNullable(cliInputDescription);
  }

  @Override
  public String getDescription() {
    return getName();
  }

  public void removeCommand(Command command) {
    commands.remove(command);
  }
}
