package br.dubidubibank.validation.validators;

import br.dubidubibank.entities.Account;
import br.dubidubibank.enums.AccountTypeCode;
import br.dubidubibank.validation.constraints.ValidNumbers;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidNumbersValidator implements ConstraintValidator<ValidNumbers, Account> {
  @Override
  public boolean isValid(Account value, ConstraintValidatorContext constraintValidatorContext) {
    if (value.getType().getCode() == AccountTypeCode.ANONYMOUS) {
      return value.getAgencyNumber() == 0 && value.getNumber() == 0;
    }
    return value.getAgencyNumber() > 0 && value.getNumber() > 0;
  }
}
