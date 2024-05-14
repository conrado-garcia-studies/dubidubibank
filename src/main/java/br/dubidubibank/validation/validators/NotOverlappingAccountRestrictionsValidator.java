package br.dubidubibank.validation.validators;

import br.dubidubibank.entities.Restriction;
import br.dubidubibank.validation.constraints.NotOverlappingAccountRestrictions;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotOverlappingAccountRestrictionsValidator
    implements ConstraintValidator<NotOverlappingAccountRestrictions, Restriction> {
  @Override
  public boolean isValid(Restriction value, ConstraintValidatorContext constraintValidatorContext) {
    return hasNoOverlappingAccountRestriction(value);
  }

  private boolean hasNoOverlappingAccountRestriction(Restriction one) {
    return one.getAccount().getRestrictions().stream() //
        .filter(
            tested ->
                !one.getId().equals(tested.getId())
                    && tested.getCommand().equals(one.getCommand())) //
        .noneMatch(other -> isTimeRangesOverlap(one, other));
  }

  private boolean isTimeRangesOverlap(Restriction one, Restriction other) {
    return (one.getStartTime().isBefore(other.getEndTime())
            || one.getStartTime().equals(other.getEndTime()))
        && (one.getEndTime().isAfter(other.getStartTime())
            || one.getEndTime().equals(other.getStartTime()));
  }
}
