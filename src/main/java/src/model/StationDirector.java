package src.model;

import src.enums.SecurityLevel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class StationDirector extends Scientist{

    @NotNull
    @Column(name = "years_of_experience", nullable = false)
    private int yearsOfExperience;

    @NotNull
    @Column(name = "security_clearance_level", nullable = false)
    private SecurityLevel securityClearanceLevel;

    @NotNull
    @Column(name = "performance_rating", nullable = false)
    @Min(1)
    @Max(10)
    private Double performanceRating;

    @ManyToMany(mappedBy = "managedBy", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<EarthStation> manages = new HashSet<>();

    public void addEarthStation(EarthStation station) {
        manages.add(station);
        if (!station.getManagedBy().contains(this)) {
            station.addManagedBy(this);
        }
    }

    public void removeEarthStation(EarthStation station) {
        manages.remove(station);
        if (station.getManagedBy().contains(this)) {
            station.removeManagedBy(this);
        }
    }
}
