package org.example.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ObservationResponse {
    private UUID id;
    private UUID patientId;
    private UUID encounterId;
    private String code;
    private String value;
    private LocalDateTime effectiveDateTime;
}
