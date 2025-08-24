package src.constraint;


import src.validator.UniqueMissionCodeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueMissionCodeValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface MissionInfoConstraint {
    String message() default "Kod misji musi być unikalny";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
