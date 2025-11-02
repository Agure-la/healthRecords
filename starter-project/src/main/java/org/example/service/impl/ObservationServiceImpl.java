package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ObservationResponse;
import org.example.entity.Observation;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.ObservationRepository;
import org.example.repository.PatientRepository;
import org.example.service.ObservationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ObservationServiceImpl implements ObservationService {

    private final ObservationRepository observationRepository;
    private final PatientRepository patientRepository;

    @Override
    public List<ObservationResponse> getObservationsByPatientId(UUID patientId) {
        log.info("Fetching observations for patient ID: {}", patientId);

        // Validate that patient exists
        patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        List<Observation> observations = observationRepository.findByPatientIdOrderByEffectiveDateTimeDesc(patientId);

        return observations.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ObservationResponse mapToResponse(Observation observation) {
        ObservationResponse response = new ObservationResponse();
        response.setId(observation.getId());
        response.setPatientId(observation.getPatient().getId());
        response.setEncounterId(observation.getEncounter() != null ? observation.getEncounter().getId() : null);
        response.setCode(observation.getCode());
        response.setValue(observation.getValue());
        response.setEffectiveDateTime(observation.getEffectiveDateTime());
        return response;
    }
}
