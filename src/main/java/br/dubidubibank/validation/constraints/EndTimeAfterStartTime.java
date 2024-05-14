package br.dubidubibank.validation.constraints;

import br.dubidubibank.validation.validators.EndTimeAfterStartTimeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EndTimeAfterStartTimeValidator.class)
public @interface EndTimeAfterStartTime {
  Class<?>[] groups() default {};

  String message() default "must have end time after start time";

  Class<? extends Payload>[] payload() default {};
}
