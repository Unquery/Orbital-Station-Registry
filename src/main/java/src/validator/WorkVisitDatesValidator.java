package src.validator;

import src.constraint.WorkVisitConstraint;
import src.model.WorkVisit;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WorkVisitDatesValidator implements ConstraintValidator<WorkVisitConstraint, WorkVisit> {

    @Override
    public boolean isValid(WorkVisit workVisit, ConstraintValidatorContext ctx) {
        if (workVisit == null) {
            return true;
        }

        boolean isDateValid  = true;
        if (workVisit.getDepartureDate() != null) {
            isDateValid = !workVisit.getArrivalDate().isAfter(workVisit.getDepartureDate());
        }

        return isDateValid;
    }
}
