package br.dubidubibank.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class Transaction extends AbstractEntity {
  @NonNull @NotNull private Double amount;

  @JoinColumn(name = "command_id")
  @ManyToOne
  @NonNull
  @NotNull
  private Command command;

  @NonNull @NotNull private Instant creationInstant;

  @JoinColumn(name = "source_account_id")
  @ManyToOne
  @NonNull
  @NotNull
  private Account sourceAccount;

  @JoinColumn(name = "target_account_id")
  @ManyToOne
  @NonNull
  @NotNull
  private Account targetAccount;

  public Transaction(Double amount, Command command, Account sourceAccount, Account targetAccount) {
    this.amount = amount;
    this.command = command;
    this.creationInstant = Instant.now();
    this.sourceAccount = sourceAccount;
    this.targetAccount = targetAccount;
  }
}
