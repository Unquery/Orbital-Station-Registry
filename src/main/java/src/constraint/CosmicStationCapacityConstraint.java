package src.constraint;


import src.validator.CosmicStationCapacityValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CosmicStationCapacityValidator.class)
@Documented
public @interface CosmicStationCapacityConstraint {
    String message() default "missionCapacityForType must be less than missionCapacity";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
