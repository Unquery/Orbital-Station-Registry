package src.validator;

import src.constraint.MissionInfoConstraint;
import src.data.MissionInfo;
import src.repository.MissionRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueMissionCodeValidator
        implements ConstraintValidator<MissionInfoConstraint, MissionInfo> {

    @Autowired
    private MissionRepository missionRepository;

    @Override
    public boolean isValid(MissionInfo missionInfo, ConstraintValidatorContext ctx) {
        if (missionInfo == null || missionInfo.getMissionCode() == null) {
            return true;
        }

        boolean exists = missionRepository.existsByMissionCode(missionInfo.getMissionCode().trim());

        if (exists) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate("Kod misji '" + missionInfo.getMissionCode() + "' już istnieje")
                    .addPropertyNode("missionCode")
                    .addConstraintViolation();
        }

        return !exists;
    }
}
