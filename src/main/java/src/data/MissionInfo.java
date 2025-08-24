package src.data;

import src.constraint.MissionInfoConstraint;
import src.enums.MissionType;
import src.validator.MissionInfoValidationStep1;
import src.validator.MissionInfoValidationStep2;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

@Data
@MissionInfoConstraint(groups = MissionInfoValidationStep1.class)
public class MissionInfo {

    @NotNull(message = "Kod misji nie może być pusty", groups = MissionInfoValidationStep1.class)
    @Size(min = 3, max = 30, message = "Kod misji musi być od 3 do 30 znaków",
            groups = MissionInfoValidationStep1.class)
    private String missionCode;

    @NotNull(message = "Opis nie może być pusty", groups = MissionInfoValidationStep1.class)
    @Size(min = 5, max = 200, message = "Opis musi być od  5 do 200 znaków",
            groups = MissionInfoValidationStep1.class)
    private String description;

    @NotNull(message = "Planowana data rozpoczęcia nie może być pusta",
            groups = MissionInfoValidationStep1.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Instant plannedStartDate;

    @NotNull(message = "Planowana data zakączenia nie może być pusta",
            groups = MissionInfoValidationStep1.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Instant plannedEndDate;

    @NotNull(message = "Typ nie może być pusty",
            groups = MissionInfoValidationStep2.class)
    @Enumerated(EnumType.STRING)
    private MissionType type;

    @AssertTrue(message = "Planowana data rozpoczęcia musi być wcześniej niż data zakończenia",
            groups = MissionInfoValidationStep1.class)
    public boolean isPlannedDatesValid() {
        if (plannedStartDate == null || plannedEndDate == null) {
            return true;
        }
        return plannedStartDate.isBefore(plannedEndDate);
    }
}
