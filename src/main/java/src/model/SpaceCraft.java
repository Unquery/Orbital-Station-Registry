package src.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class SpaceCraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 10, max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Min(100)
    @Column(name = "mass", nullable = false)
    private Double mass;

    @NotNull
    @Min(10)
    @Column(name = "orbital_altitude", nullable = false)
    private Double orbitalAltitude;
}
