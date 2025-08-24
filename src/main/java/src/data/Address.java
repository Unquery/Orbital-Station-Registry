package src.data;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Embeddable
public class Address {
    @NotNull
    @Size(min=2, max=100)
    String street;
    @NotNull
    @Size(min=2, max=100)
    String city;
    @NotNull
    @Size(min=2, max=100)
    String state;
    @NotNull
    @Size(min=2, max=100)
    String postalCode;
    @NotNull
    @Size(min=2, max=100)
    String country;
    @NotNull
    @Min(-90)
    @Max(90)
    Double latitude;
    @NotNull
    @Min(-180)
    @Max(180)
    Double longitude;
}
