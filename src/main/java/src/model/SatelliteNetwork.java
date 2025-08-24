package src.model;


import src.enums.NetworkTopology;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SatelliteNetwork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    @Size(min = 5, max = 255)
    private String name;

    @NotNull
    @Column(name = "coverageArea", nullable = false)
    @Min(10)
    private Double coverageArea;

    @NotNull
    @Column(name = "encryption_protocol", nullable = false)
    @Size(min = 5, max = 255)
    private String encryptionProtocol;

    @NotNull
    @Column(name = "network_topology", nullable = false)
    private NetworkTopology networkTopology;


    @ManyToMany(mappedBy = "receivesInfoFrom", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    private Set<EarthStation> sendsInfoTo = new HashSet<>();

    public void addEarthStation(EarthStation station) {
        sendsInfoTo.add(station);
        if (!station.getReceivesInfoFrom().contains(this)) {
            station.addSatelliteNetwork(this);
        }
    }

    public void removeEarthStation(EarthStation station) {
        sendsInfoTo.remove(station);
        if (station.getReceivesInfoFrom().contains(this)) {
            station.removeSatelliteNetwork(this);
        }
    }

    @OneToMany(mappedBy = "worksIn", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    @Size(min = 1)
    private Set<Satellite> hasWorked = new HashSet<>();

    public void addSatellite(Satellite satellite) {
        hasWorked.add(satellite);
        if (satellite.getWorksIn() != this) {
            satellite.setNetwork(this);
        }
    }

    public void removeSatellite(Satellite satellite) {
        hasWorked.remove(satellite);
        if (satellite.getWorksIn() == this) {
            satellite.removeNetwork();
        }
    }



}
