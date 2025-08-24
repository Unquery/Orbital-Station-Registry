package src.validator;

import src.constraint.MissionDatesConstraint;
import src.model.Mission;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MissionDateValidator implements ConstraintValidator<MissionDatesConstraint, Mission> {

    @Override
    public boolean isValid(Mission mission, ConstraintValidatorContext ctx) {
        if (mission == null) {
            return true;
        }

        boolean isPlannedDateValid =
                !mission.getPlannedStartDate().isAfter(mission.getPlannedEndDate());
        boolean isActualDateValid  = true;
        if (mission.getStartDate() != null && mission.getEndDate() != null) {
            isActualDateValid = !mission.getStartDate().isAfter(mission.getEndDate());
        }

        return isPlannedDateValid && isActualDateValid;
    }
}
