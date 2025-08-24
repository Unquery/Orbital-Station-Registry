package src.validator;

import src.constraint.CosmicStationCapacityConstraint;
import src.model.CosmicStation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CosmicStationCapacityValidator
        implements ConstraintValidator<CosmicStationCapacityConstraint, CosmicStation> {

    @Override
    public boolean isValid(CosmicStation station, ConstraintValidatorContext ctx) {
        if (station == null) {
            return true;
        }
        int cap = station.getMissionCapacity();
        int capForType = station.getMissionCapacityForType();

        return capForType < cap;


    }
}
