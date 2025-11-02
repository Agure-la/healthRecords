package org.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for returning patient data in responses
 */
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
    
    public static PatientResponse fromEntity(Patient patient) {
        if (patient == null) {
            return null;
        }
        
        return PatientResponse.builder()
                .id(patient.getId())
                .identifier(patient.getIdentifier())
                .givenName(patient.getGivenName())
                .familyName(patient.getFamilyName())
                .birthDate(patient.getBirthDate())
                .gender(patient.getGender())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .build();
    }
}
