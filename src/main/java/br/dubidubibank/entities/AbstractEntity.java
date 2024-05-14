package br.dubidubibank.entities;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;
import lombok.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
@ToString
public abstract class AbstractEntity implements Serializable {
  @Serial private static final long serialVersionUID = 1L;

  @Column(unique = true)
  @GeneratedValue(strategy = GenerationType.TABLE)
  @Getter(AccessLevel.NONE)
  @EqualsAndHashCode.Include
  @Id
  protected Long id;

  public Optional<Long> getId() {
    return Optional.ofNullable(id);
  }
}
