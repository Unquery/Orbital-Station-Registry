package src.constraint;

import src.validator.MissionDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MissionDateValidator.class)
@Documented
public @interface MissionDatesConstraint {
    String message() default "Mission dates are invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
