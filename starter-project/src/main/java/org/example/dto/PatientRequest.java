package org.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.example.entity.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for creating or updating a patient
 */
@Data
public class PatientRequest {
    
    @NotBlank(message = "Identifier is required")
    @Size(max = 50, message = "Identifier must be less than 50 characters")
    private String identifier;

    @NotBlank(message = "Given name is required")
    @Size(max = 100, message = "Given name must be less than 100 characters")
    private String givenName;

    @NotBlank(message = "Family name is required")
    @Size(max = 100, message = "Family name must be less than 100 characters")
    private String familyName;

    @NotNull(message = "Birth date is required")
    @PastOrPresent(message = "Birth date must be in the past or present")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotBlank(message = "Username is required")
    @Size(max = 50, message = "Username must be less than 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must be less than 100 characters")
    private String email;

    private Patient.Gender gender;

    private List<EncounterRequest> encounters;

    private List<ObservationRequest> observations;

    @Data
    public static class EncounterRequest {
        @NotNull(message = "Start date/time is required")
        private LocalDateTime start;

        private LocalDateTime endTime;

        @NotBlank(message = "Encounter class is required")
        private String encounterClass;

        private List<ObservationRequest> observations;
    }

    @Data
    public static class ObservationRequest {
        @NotBlank(message = "Code is required")
        private String code;

        @NotBlank(message = "Value is required")
        private String value;

        @NotNull(message = "Effective date/time is required")
        private LocalDateTime effectiveDateTime;
    }
}
