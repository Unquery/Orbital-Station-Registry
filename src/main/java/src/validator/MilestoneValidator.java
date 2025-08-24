package src.validator;

import src.constraint.MilestoneConstraint;
import src.model.Milestone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MilestoneValidator implements ConstraintValidator<MilestoneConstraint, Milestone> {

    @Override
    public boolean isValid(Milestone milestone, ConstraintValidatorContext cxt) {
        if(milestone == null){
            return true;
        }

        if(milestone.getAssignedTo().getStartDate() == null){
            return false;
        }

        if(milestone.getDateOfAccomplishing().isAfter(milestone.getAssignedTo().getStartDate())){
            return false;
        }

        double completed = milestone.getAssignedTo().getCompletedPercent();
        double remaining = 100.0 - completed;

        return milestone.getPercentageToAdd() >= 0 && !(milestone.getPercentageToAdd() > remaining);
    }
}
