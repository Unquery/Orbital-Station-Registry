package src.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "age", nullable = false)
    private String age;

    @NotBlank(message = "Email may not be blank")
    @Email(message = "Please provide a valid email address")
    @Size(min = 5, max = 100, message = "Email must be between 5 and 100 characters")
    @NotNull
    private String email;

    @NotBlank(message = "Password may not be blank")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Column(nullable = false)
    @NotNull
    private String password;
}
