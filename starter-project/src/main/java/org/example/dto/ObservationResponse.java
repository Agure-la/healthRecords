package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ObservationResponse {
    private UUID id;
    private UUID patientId;
    private UUID encounterId;
    private String code;
    private String value;
    private LocalDateTime effectiveDateTime;
}
