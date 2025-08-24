package src.model;

import src.enums.PortType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DockingStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "port_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PortType portType;

    @NotNull
    @Column(name = "is_free", nullable = false)
    private Boolean isFree;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false, updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private CosmicStation ownedBy;

    public void setOwnedBy(CosmicStation station) {
        this.ownedBy = station;
        if (!station.getOwns().contains(this)) {
            station.addDockingStation(this);
        }
    }

    public void removeOwnedBy() {
        CosmicStation old = this.ownedBy;
        this.ownedBy = null;
        if (old != null && old.getOwns().contains(this)) {
            old.removeDockingStation(this);
        }
    }
}
