package br.dubidubibank.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true)
public class Person extends AbstractEntity implements DescriptedEntity {
  @Column(unique = true)
  @EqualsAndHashCode.Include
  @Email
  @NonNull
  @NotNull
  @Size(max = 255)
  private String email;

  @NotEmpty
  @NonNull
  @NotNull
  @Size(max = 255)
  private String name;

  @Override
  public String getDescription() {
    return String.format("%s (%s)", getName(), getEmail());
  }
}
