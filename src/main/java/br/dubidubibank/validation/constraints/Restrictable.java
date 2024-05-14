package br.dubidubibank.validation.constraints;

import br.dubidubibank.validation.validators.RestrictableValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RestrictableValidator.class)
public @interface Restrictable {
  Class<?>[] groups() default {};

  String message() default "must have a restrictable command";

  Class<? extends Payload>[] payload() default {};
}
