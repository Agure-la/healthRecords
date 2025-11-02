package org.example.service;

import org.example.dto.ObservationResponse;

import java.util.List;
import java.util.UUID;

public interface ObservationService {
    List<ObservationResponse> getObservationsByPatientId(UUID patientId);
}
