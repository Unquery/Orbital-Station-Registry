package src.model;


import src.enums.TrainingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Astronaut extends Person{

    @ElementCollection
    @CollectionTable(name = "languages", joinColumns = @JoinColumn(name = "language_id"))
    @Builder.Default
    @NotNull
    @Size(min = 1)
    private final List<
            @NotBlank
            @Size(min = 2, max = 100)  String
            > languageSpoken = new ArrayList<>();

    @NotNull
    @Column(name = "training_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TrainingStatus trainingStatus;

    @OneToOne(mappedBy = "worker",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private WorkVisit workVisit;

    public void setWorkVisit(CosmicStation cosmicStation) {
        WorkVisit workVisit = new WorkVisit(this, cosmicStation);
        this.workVisit = workVisit;
        cosmicStation.getAccommodates().add(workVisit);
    }

    public void setWorkVisit(WorkVisit workVisit) {
        this.workVisit = workVisit;
    }

    public void removeWorkVisit() {
        workVisit.getAccommodation().getAccommodates().remove(workVisit);
        this.workVisit = null;
    }

}
