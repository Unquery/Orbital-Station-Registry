package src.model;

import src.enums.Specialization;
import jakarta.persistence.*;
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
public class Scientist extends Person {


    @NotNull
    @Column(name = "salary", nullable = false)
    @Min(1000)
    @Max(10000)
    private Double salary;

    @NotNull
    @Column(name = "specialization", nullable = false)
    private Specialization specialization;


    @ManyToMany(mappedBy = "workers", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<EarthStation> worksIn = new HashSet<>();


    public void addEarthStation(EarthStation station) {
        worksIn.add(station);
        if (!station.getWorkers().contains(this)) {
            station.addWorker(this);
        }
    }

    public void removeEarthStation(EarthStation station) {
        worksIn.remove(station);
        if (station.getWorkers().contains(this)) {
            station.removeWorker(this);
        }
    }
}
