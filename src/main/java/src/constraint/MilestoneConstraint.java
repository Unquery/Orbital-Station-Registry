package src.constraint;


import src.validator.MilestoneValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MilestoneValidator.class)
@Documented
public @interface MilestoneConstraint {
    String message() default "Invalid milestone";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
