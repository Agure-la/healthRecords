package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients", indexes = {
        @Index(name = "idx_patient_identifier", columnList = "identifier", unique = true),
        @Index(name = "idx_patient_name", columnList = "familyName, givenName"),
        @Index(name = "idx_patient_birth_date", columnList = "birthDate")
    })
@Getter
@Setter
public class Patient extends BaseEntity {

    @NotBlank(message = "Identifier is required")
    @Size(max = 50, message = "Identifier must be less than 50 characters")
    @Column(nullable = false, unique = true, length = 50)
    private String identifier;

    @NotBlank(message = "Given name is required")
    @Size(max = 100, message = "Given name must be less than 100 characters")
    @Column(name = "given_name", nullable = false, length = 100)
    private String givenName;

    @NotBlank(message = "Family name is required")
    @Size(max = 100, message = "Family name must be less than 100 characters")
    @Column(name = "family_name", nullable = false, length = 100)
    private String familyName;

    @NotNull(message = "Birth date is required")
    @PastOrPresent(message = "Birth date must be in the past or present")
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @NotBlank(message = "Username is required")
    @Size(max = 50, message = "Username must be less than 50 characters")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must be less than 100 characters")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Gender gender;

    public enum Gender {
        MALE, FEMALE, OTHER, UNKNOWN
    }

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Encounter> encounters = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Observation> observations = new ArrayList<>();

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + getId() +
                ", identifier='" + identifier + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", birthDate=" + birthDate +
                ", gender=" + gender +
                '}';
    }
}
