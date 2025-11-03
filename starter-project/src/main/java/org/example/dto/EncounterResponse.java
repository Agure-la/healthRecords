package org.example.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EncounterResponse {
    private UUID id;
    private LocalDateTime start;
    private LocalDateTime endTime;
    private String encounterClass;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID patientId;
    private List<ObservationResponse> observations;
}
