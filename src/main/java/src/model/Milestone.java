package src.model;


import src.constraint.MilestoneConstraint;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@MilestoneConstraint
public class Milestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date_of_accomplishing", nullable = false)
    private Instant dateOfAccomplishing;

    @NotNull
    @Column(name = "percentage_to_add", nullable = false)
    private Float percentageToAdd;

    @NotNull
    @Column(name = "description", nullable = false)
    @Size(min = 10, max = 1000)
    private String description;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false, updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Mission assignedTo;

    public void setAssignedTo(Mission mission) {
        this.assignedTo = mission;
        if (mission != null && !mission.getHasAssigned().contains(this)) {
            mission.addHasAssigned(this);
        }
    }

    public void removeAssignedTo() {
        if (assignedTo != null) {
            Mission old = this.assignedTo;
            this.assignedTo = null;
            if (old.getHasAssigned().contains(this)) {
                old.removeHasAssigned(this);
            }
        }
    }


    @PrePersist
    public void prePersist() {
        Mission mission = getAssignedTo();
        double newPct = mission.getCompletedPercent() + getPercentageToAdd();
        mission.setCompletedPercent(newPct);
    }
}
