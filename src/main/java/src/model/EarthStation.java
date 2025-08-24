package src.model;

import src.data.Address;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EarthStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Embedded
    @NotNull
    @Column(name = "address", nullable = false)
    private Address address;

    @Min(5)
    @Column(name = "capacity", nullable = false)
    private int capacity;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "earth_station_scientist",
            joinColumns = @JoinColumn(name = "earth_station_id"),
            inverseJoinColumns = @JoinColumn(name = "scientist_id")
    )
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    private Set<Scientist> workers = new HashSet<>();

    public void addWorker(Scientist scientist) {
        workers.add(scientist);
        if (!scientist.getWorksIn().contains(this)) {
            scientist.addEarthStation(this);
        }
    }

    public void removeWorker(Scientist scientist) {
        workers.remove(scientist);
        if (scientist.getWorksIn().contains(this)) {
            scientist.removeEarthStation(this);
        }
    }


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "earth_station_station_director",
            joinColumns = @JoinColumn(name = "earth_station_id"),
            inverseJoinColumns = @JoinColumn(name = "station_director_id")
    )
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    private Set<StationDirector> managedBy = new HashSet<>();

    public void addManagedBy(StationDirector director) {
        managedBy.add(director);
        if (!director.getManages().contains(this)) {
            director.addEarthStation(this);
        }
    }

    public void removeManagedBy(StationDirector director) {
        managedBy.remove(director);
        if (director.getManages().contains(this)) {
            director.removeEarthStation(this);
        }
    }

    @ManyToMany(mappedBy = "operatedBy", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    private Set<CosmicStation> operates = new HashSet<>();

    public void addCosmicStation(CosmicStation station) {
        operates.add(station);
        if (!station.getOperatedBy().contains(this)) {
            station.addOperatedBy(this);
        }
    }

    public void removeCosmicStation(CosmicStation station) {
        operates.remove(station);
        if (station.getOperatedBy().contains(this)) {
            station.removeOperatedBy(this);
        }
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "earth_station_satellite_network",
            joinColumns = @JoinColumn(name = "earth_station_id"),
            inverseJoinColumns = @JoinColumn(name = "satellite_network")
    )
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    private Set<SatelliteNetwork> receivesInfoFrom = new HashSet<>();

    public void addSatelliteNetwork(SatelliteNetwork network) {
        receivesInfoFrom.add(network);
        if (!network.getSendsInfoTo().contains(this)) {
            network.addEarthStation(this);
        }
    }

    public void removeSatelliteNetwork(SatelliteNetwork network) {
        receivesInfoFrom.remove(network);
        if (network.getSendsInfoTo().contains(this)) {
            network.removeEarthStation(this);
        }
    }
}
