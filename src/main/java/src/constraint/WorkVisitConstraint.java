package src.constraint;

import src.validator.WorkVisitDatesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WorkVisitDatesValidator.class)
@Documented
public @interface WorkVisitConstraint {
    String message() default "WorkVisit dates are invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
