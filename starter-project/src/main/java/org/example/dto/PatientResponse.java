package org.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.Encounter;
import org.example.entity.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientResponse {

    private UUID id;
    private String identifier;
    private String givenName;
    private String familyName;
    private String username;
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private Patient.Gender gender;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private List<EncounterResponse> encounters;
    private List<ObservationResponse> observations;

    public static PatientResponse fromEntity(Patient patient, boolean includeRelations) {
        if (patient == null) return null;

        List<EncounterResponse> encounterResponses = null;
        List<ObservationResponse> observationResponses = null;

        if (includeRelations && patient.getEncounters() != null) {
            encounterResponses = patient.getEncounters().stream()
                    .map(enc -> EncounterResponse.builder()
                            .id(enc.getId())
                            .patientId(patient.getId())
                            .start(enc.getStart())
                            .end(enc.getEndTime())
                            .encounterClass(enc.getEncounterClass())
                            .build())
                    .collect(Collectors.toList());
        }

        if (includeRelations && patient.getObservations() != null) {
            observationResponses = patient.getObservations().stream()
                    .map(obs -> ObservationResponse.builder()
                            .id(obs.getId())
                            .patientId(patient.getId())
                            .encounterId(obs.getEncounter() != null ? obs.getEncounter().getId() : null)
                            .code(obs.getCode())
                            .value(obs.getValue())
                            .effectiveDateTime(obs.getEffectiveDateTime())
                            .build())
                    .collect(Collectors.toList());
        }

        return PatientResponse.builder()
                .id(patient.getId())
                .identifier(patient.getIdentifier())
                .givenName(patient.getGivenName())
                .familyName(patient.getFamilyName())
                .username(patient.getUsername())
                .email(patient.getEmail())
                .birthDate(patient.getBirthDate())
                .gender(patient.getGender())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .encounters(encounterResponses)
                .observations(observationResponses)
                .build();
    }

    @Data
    @Builder
    public static class EncounterResponse {
        private UUID id;
        private UUID patientId;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime start;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime end;
        private Encounter.EncounterClass encounterClass;
    }

    @Data
    @Builder
    public static class ObservationResponse {
        private UUID id;
        private UUID patientId;
        private UUID encounterId;
        private String code;
        private String value;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime effectiveDateTime;
    }
}

