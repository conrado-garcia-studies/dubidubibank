package br.dubidubibank.validation.validators;

import br.dubidubibank.entities.Restriction;
import br.dubidubibank.validation.constraints.EndTimeAfterStartTime;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EndTimeAfterStartTimeValidator
    implements ConstraintValidator<EndTimeAfterStartTime, Restriction> {
  @Override
  public boolean isValid(Restriction value, ConstraintValidatorContext constraintValidatorContext) {
    return value.getEndTime().isAfter(value.getStartTime());
  }
}
