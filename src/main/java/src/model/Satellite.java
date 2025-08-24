package src.model;

import src.enums.SatelliteStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
public class Satellite extends SpaceCraft {

    @NotNull
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private SatelliteStatus status;

    @ElementCollection
    @CollectionTable(name = "solar_panels", joinColumns = @JoinColumn(name = "solar_panel_id"))
    @Builder.Default
    @NotNull
    @Column(nullable = false)
    private final List<
            @NotBlank
            @Size(min = 5, max = 100)
            @NotNull String
            > solarPanels = new ArrayList<>();

    @NotBlank
    @Size(min = 5, max = 150)
    @Column(name = "manufacturer", nullable = false)
    private String manufacturer;

    @Min(400)
    @Column(name = "power_capacity_w", nullable = false)
    private Double powerCapacityW;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "satellite_network_id")
    private SatelliteNetwork worksIn;

    public void setNetwork(SatelliteNetwork network) {
        this.worksIn = network;
        if (network != null && !network.getHasWorked().contains(this)) {
            network.addSatellite(this);
        }
    }

    public void removeNetwork() {
        if (this.worksIn != null) {
            SatelliteNetwork old = this.worksIn;
            this.worksIn = null;
            if (old.getHasWorked().contains(this)) {
                old.removeSatellite(this);
            }
        }
    }
}
