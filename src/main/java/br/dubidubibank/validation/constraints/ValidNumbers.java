package br.dubidubibank.validation.constraints;

import br.dubidubibank.validation.validators.ValidNumbersValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidNumbersValidator.class)
public @interface ValidNumbers {
  Class<?>[] groups() default {};

  String message() default
      "must have agency number and number equal to zero if type is ANONYMOUS and positive agency number and number"
          + " otherwise";

  Class<? extends Payload>[] payload() default {};
}
