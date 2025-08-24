package src.model;


import src.constraint.WorkVisitConstraint;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "work_visits")
@WorkVisitConstraint
public class WorkVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @CreationTimestamp
    @Column(name = "arrival_date", nullable = false)
    private Instant arrivalDate;

    @Column(name = "departure_date")
    private Instant departureDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "astronaut_id", nullable = false)
    @NotNull
    private Astronaut worker;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cosmic_station_id", nullable = false)
    @NotNull
    private CosmicStation accommodation;


    public WorkVisit(Astronaut astronaut, CosmicStation cosmicStation) {
        this.worker = astronaut;
        this.accommodation = cosmicStation;
    }
}
