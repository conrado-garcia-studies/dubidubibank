package br.dubidubibank.validation.validators;

import br.dubidubibank.entities.Command;
import br.dubidubibank.validation.constraints.Restrictable;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RestrictableValidator implements ConstraintValidator<Restrictable, Command> {
  @Override
  public boolean isValid(Command value, ConstraintValidatorContext constraintValidatorContext) {
    return value.getRestrictable();
  }
}
