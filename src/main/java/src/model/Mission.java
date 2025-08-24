package src.model;

import src.constraint.MissionDatesConstraint;
import src.data.MissionInfo;
import src.enums.MissionStatus;
import src.enums.MissionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@MissionDatesConstraint
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "mission_code", nullable = false, unique = true)
    @Size(min = 3, max = 30)
    private String missionCode;

    @NotNull
    @Column(name = "description", nullable = false)
    @Size(min = 5, max = 200)
    private String description;

    @NotNull
    @Column(name = "planned_start_date", nullable = false)
    private Instant plannedStartDate;

    @NotNull
    @Column(name = "planned_end_date", nullable = false)
    private Instant plannedEndDate;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "is_successful")
    private Boolean isSuccessful;

    @NotNull
    @Column(name = "completed_percent", nullable = false)
    @Min(0)
    @Max(100)
    private Double completedPercent;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private MissionType type;

    @Builder.Default
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MissionStatus status = MissionStatus.PLANNED;

    @OneToMany(mappedBy = "assignedTo", cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    private Set<Milestone> hasAssigned = new HashSet<>();

    public void addHasAssigned(Milestone milestone) {
        hasAssigned.add(milestone);
        if (milestone.getAssignedTo() != this) {
            milestone.setAssignedTo(this);
        }
    }

    public void removeHasAssigned(Milestone milestone) {
        if (hasAssigned.remove(milestone)) {
            if (milestone.getAssignedTo() == this) {
                milestone.removeAssignedTo();
            }
        }
    }

    @ManyToMany(mappedBy = "performedOn", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    private Set<CosmicStation> performedBy = new HashSet<>();

    public void addPerformedBy(CosmicStation station) {
        performedBy.add(station);
        if (!station.getPerformedOn().containsKey(this.getMissionCode())) {
            station.addPerformedOn(this);
        }
    }

    public void removePerformedBy(CosmicStation station) {
        performedBy.remove(station);
        if (station.getPerformedOn().containsKey(this.getMissionCode())) {
            station.removePerformedOn(this);
        }
    }

    @PreRemove
    private void preRemove() {
        for (CosmicStation station : new HashSet<>(performedBy)) {
            station.removePerformedOn(this);
        }
    }

    /**
     * Tworzy nową misję na podstawie przekazanych danych.
     * <p>
     * Ustawia kod, opis, daty planowane, typ misji i ustawia domyślnie status PLANNED.
     *
     * @param info dane misji
     */
    public void createMission(MissionInfo info) {
        this.missionCode = info.getMissionCode();
        this.description = info.getDescription();
        this.plannedStartDate = info.getPlannedStartDate();
        this.plannedEndDate = info.getPlannedEndDate();
        this.type = info.getType();
        this.status = MissionStatus.PLANNED;
        this.completedPercent = 0.0;
        this.startDate = null;
        this.endDate = null;
        this.isSuccessful = null;
    }

    /**
     * Rozpoczyna misję.
     * <p>
     * Może być uruchomiona tylko, jeśli jej status to PLANNED lub PAUSED.
     * Jeśli misja była w stanie PLANNED, zapisuje datę startu.
     *
     * @throws IllegalStateException jeśli misja nie może być rozpoczęta
     */
    public void startMission() {
        if (!(status == MissionStatus.PLANNED || status == MissionStatus.PAUSED)) {
            throw new IllegalStateException("Can only start a PLANNED or PAUSED mission");
        }

        if(this.status == MissionStatus.PLANNED) {
            this.startDate = Instant.now();
        }

        this.status = MissionStatus.INPROGRESS;
    }


    /**
     * Wstrzymuje trwającą misję.
     * <p>
     * Można wstrzymać tylko misję ze statusem INPROGRESS.
     *
     * @throws IllegalStateException jeśli misja nie jest w INPROGRESS
     */
    public void pauseMission() {
        if (status != MissionStatus.INPROGRESS) {
            throw new IllegalStateException("Can only pause an INPROGRESS mission");
        }
        this.status = MissionStatus.PAUSED;
    }


    /**
     * Anuluje trwającą lub wstrzymaną misję.
     * <p>
     * Ustawia status CANCELED, datę zakończenia i oznacza jako nieudaną.
     *
     * @throws IllegalStateException jeśli misja nie jest INPROGRESS ani PAUSED
     */
    public void cancelMission() {
        if (!(status == MissionStatus.INPROGRESS || status == MissionStatus.PAUSED)) {
            throw new IllegalStateException("Can only cancel an INPROGRESS or PAUSED mission");
        }
        this.status = MissionStatus.CANCELED;
        this.endDate = Instant.now();
        this.isSuccessful = false;
    }


    /**
     * Przywraca anulowaną misję do planowania.
     * <p>
     * Czyści dane startu, zakończenia, sukcesu i ustawia status na PLANNED.
     *
     * @throws IllegalStateException jeśli misja nie ma statusu CANCELED
     */
    public void planMission() {

        if(status != MissionStatus.CANCELED){
            throw new IllegalStateException("Can only plan an CANCELED mission");
        }
        this.status = MissionStatus.PLANNED;
        this.startDate = null;
        this.endDate = null;
        this.isSuccessful = null;
        this.completedPercent = 0.0;
    }


    /**
     * Kończy aktualnie trwającą misję.
     * <p>
     * Ustawia datę zakończenia, status COMPLETED oraz oznacza sukces,
     * jeśli ukończenie wynosi 100%.
     *
     * @throws IllegalStateException jeśli misja nie jest INPROGRESS
     */
    public void endMission() {
        if (status != MissionStatus.INPROGRESS) {
            throw new IllegalStateException("Can only end a INPROGRESS mission");
        }
        this.status = MissionStatus.COMPLETED;
        this.endDate = Instant.now();
        this.isSuccessful = BigDecimal.valueOf(completedPercent)
                                      .compareTo(BigDecimal.valueOf(100.0)) == 0;
    }
}
