package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EncounterRequest {
    @NotNull(message = "Start date/time is required")
    private LocalDateTime start;

    private LocalDateTime end;

    @NotBlank(message = "Encounter class is required")
    private String encounterClass;

    private List<ObservationRequest> observations;
}
