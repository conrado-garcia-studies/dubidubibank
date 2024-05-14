package br.dubidubibank.validation.constraints;

import br.dubidubibank.validation.validators.NotOverlappingAccountRestrictionsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotOverlappingAccountRestrictionsValidator.class)
public @interface NotOverlappingAccountRestrictions {
  Class<?>[] groups() default {};

  String message() default "must not overlap account restrictions";

  Class<? extends Payload>[] payload() default {};
}
