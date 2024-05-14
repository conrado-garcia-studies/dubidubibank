package br.dubidubibank.entities;

import br.dubidubibank.validation.constraints.EndTimeAfterStartTime;
import br.dubidubibank.validation.constraints.NotOverlappingAccountRestrictions;
import br.dubidubibank.validation.constraints.Restrictable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalTime;
import lombok.*;

@Data
@EndTimeAfterStartTime
@Entity
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@NotOverlappingAccountRestrictions
@NoArgsConstructor
@RequiredArgsConstructor
@Table(
    uniqueConstraints =
        @UniqueConstraint(columnNames = {"account_id", "command_id", "end_time", "start_time"}))
@ToString(callSuper = true)
public class Restriction extends AbstractEntity implements DescriptedEntity {
  @EqualsAndHashCode.Include
  @JoinColumn(name = "account_id")
  @ManyToOne
  @NonNull
  @NotNull
  private Account account;

  @NonNull @NotNull @PositiveOrZero private Double amount;

  @EqualsAndHashCode.Include
  @JoinColumn(name = "command_id")
  @ManyToOne
  @NonNull
  @NotNull
  @Restrictable
  private Command command;

  @Column(precision = 3)
  @EqualsAndHashCode.Include
  @NonNull
  @NotNull
  private LocalTime endTime;

  @Column(precision = 3)
  @EqualsAndHashCode.Include
  @NonNull
  @NotNull
  private LocalTime startTime;

  @Override
  public String getDescription() {
    return String.format(
        "%s of $%.2f from %s to %s",
        getCommand().getDescription(), getAmount(), getStartTime(), getEndTime());
  }
}
