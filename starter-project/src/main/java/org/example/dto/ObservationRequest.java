package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ObservationRequest {
    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Value is required")
    private String value;

    @NotNull(message = "Effective date/time is required")
    private LocalDateTime effectiveDateTime;
}
