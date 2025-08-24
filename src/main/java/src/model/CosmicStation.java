package src.model;


import src.constraint.CosmicStationCapacityConstraint;
import src.enums.MissionType;
import src.enums.OperationalStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@CosmicStationCapacityConstraint
public class CosmicStation extends SpaceCraft{

    @Min(10)
    @Column(name = "mission_capacity", nullable = false)
    private int missionCapacity;

    @Min(1)
    @Column(name = "mission_capacity_for_type", nullable = false)
    private int missionCapacityForType;

    @NotNull
    @Column(name = "launch_date", nullable = false)
    private Instant launchDate;

    @NotNull
    @Column(name = "operational_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OperationalStatus operationalStatus;

    @NotNull
    @Min(30)
    @Column(name = "power_capacity_kw", nullable = false)
    private Double powerCapacityKW;


    @OneToMany(mappedBy = "dockedOn", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    private Set<ScientificModule> hasDocked = new HashSet<>();

    public void addScientificModule(ScientificModule module) {
        hasDocked.add(module);
        if (module.getDockedOn() != this) {
            module.setDockedOn(this);
        }
    }

    public void removeScientificModule(ScientificModule module) {
        hasDocked.remove(module);
        if (module.getDockedOn() == this) {
            module.removeDockedOn();
        }
    }


    @OneToMany(mappedBy = "ownedBy", cascade = {CascadeType.REMOVE, CascadeType.PERSIST},
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    private Set<DockingStation> owns = new HashSet<>();

    public void addDockingStation(DockingStation station) {
        owns.add(station);
        if (station.getOwnedBy() != this) {
            station.setOwnedBy(this);
        }
    }

    public void removeDockingStation(DockingStation station) {
        owns.remove(station);
        if (station.getOwnedBy() == this) {
            station.removeOwnedBy();
        }
    }

    @OneToMany(mappedBy = "accommodation",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    @OrderBy("arrivalDate DESC")
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    private Set<WorkVisit> accommodates = new HashSet<>();

    public void addWorkVisit(Astronaut astronaut) {
        WorkVisit workVisit = new WorkVisit(astronaut, this);
        this.accommodates.add(workVisit);
        astronaut.setWorkVisit(workVisit);
    }

    public void removeWorkVisit(Astronaut astronaut) {
        for (Iterator<WorkVisit> iterator = accommodates.iterator();
             iterator.hasNext(); ) {
            WorkVisit workVisit = iterator.next();

            if (workVisit.getAccommodation().equals(this) &&
                    workVisit.getWorker().equals(astronaut)) {
                iterator.remove();
                workVisit.getWorker().removeWorkVisit();
            }
        }
    }


    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch   = FetchType.LAZY
    )
    @JoinTable(
            name = "cosmic_station_missions",
            joinColumns = @JoinColumn(name = "cosmic_station_id"),
            inverseJoinColumns = @JoinColumn(
                name = "mission_code",
                referencedColumnName = "mission_code"
            )
    )
    @MapKey(name = "missionCode")
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    private Map<String, Mission> performedOn = new TreeMap<>();

    public void addPerformedOn(Mission mission) {
        performedOn.put(mission.getMissionCode(), mission);
        if (!mission.getPerformedBy().contains(this)) {
            mission.addPerformedBy(this);
        }
    }

    public void removePerformedOn(Mission mission) {
        performedOn.remove(mission.getMissionCode());
        if (mission.getPerformedBy().contains(this)) {
            mission.removePerformedBy(this);
        }
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "cosmic_station_earth_station",
            joinColumns = @JoinColumn(name = "cosmic_station_id"),
            inverseJoinColumns = @JoinColumn(name = "earth_station_id")
    )
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    private Set<EarthStation> operatedBy = new HashSet<>();

    public void addOperatedBy(EarthStation station) {
        operatedBy.add(station);
        if (!station.getOperates().contains(this)) {
            station.addCosmicStation(this);
        }
    }

    public void removeOperatedBy(EarthStation station) {
        operatedBy.remove(station);
        if (station.getOperates().contains(this)) {
            station.removeCosmicStation(this);
        }
    }

    /**
     * Sprawdza, czy stacja kosmiczna ma wolne miejsce na nową misję danego typu.
     * <p>
     * Zwraca true, jeśli:
     * - liczba wszystkich misji jest mniejsza niż maksymalna pojemność,
     * - oraz liczba misji danego typu jest mniejsza niż limit dla tego typu.
     *
     * @param type typ misji do sprawdzenia
     * @return true, jeśli stacja może przyjąć misję; false w przeciwnym razie
     */

    public Boolean isHasCapacityForMission(MissionType type) {
        int totalPerformedMissions = performedOn.size();
        if (totalPerformedMissions >= missionCapacity) {
            return false;
        }

        long totalMissionsForType = performedOn.values().stream()
                .filter(m -> m.getType() == type)
                .count();
        return totalMissionsForType < missionCapacityForType;
    }
}
