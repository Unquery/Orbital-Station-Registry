package src.model;


import src.enums.TypeOfResearch;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScientificModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(100)
    @NotNull
    @Column(name = "power_consumption", nullable = false)
    private Double powerConsumption;

    @NotNull
    @Column(name = "type_of_research", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeOfResearch typeOfResearch;

    @NotNull
    @Min(2)
    @Column(name = "laboratory_space_area", nullable = false)
    private Double laboratorySpaceArea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cosmic_station_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private CosmicStation dockedOn;

    public void setDockedOn(CosmicStation station) {
        this.dockedOn = station;
        if (station != null && !station.getHasDocked().contains(this)) {
            station.addScientificModule(this);
        }
    }

    public void removeDockedOn() {
        if (dockedOn != null) {
            CosmicStation old = this.dockedOn;
            this.dockedOn = null;
            if (old.getHasDocked().contains(this)) {
                old.removeScientificModule(this);
            }
        }
    }
}
