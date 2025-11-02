package org.example.service.impl;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PatientRequest;
import org.example.dto.PatientResponse;
import org.example.entity.Observation;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.PatientMapper;
import org.example.entity.Encounter;
import org.example.entity.Patient;
import org.example.repository.EncounterRepository;
import org.example.repository.ObservationRepository;
import org.example.repository.PatientRepository;
import org.example.service.PatientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of the PatientService interface.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final EncounterRepository encounterRepository;
    private final PatientMapper patientMapper;
    private final ObservationRepository observationRepository;

    @Override
    @Transactional(readOnly = true)
    public PatientResponse getPatientById(UUID id) {
        log.debug("Fetching patient with ID: {}", id);
        return patientRepository.findById(id)
                .map(patientMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
    }

    private void validateUniqueFields(PatientRequest request) {
        if (patientRepository.existsByIdentifier(request.getIdentifier())) {
            throw new IllegalArgumentException(
                    "Patient with identifier '" + request.getIdentifier() + "' already exists"
            );
        }

        if (patientRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException(
                    "Patient with username '" + request.getUsername() + "' already exists"
            );
        }

        if (patientRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                    "Patient with email '" + request.getEmail() + "' already exists"
            );
        }
    }

    @Override
    @Transactional
    public PatientResponse createPatient(PatientRequest request) {
        log.info("Creating new patient with identifier: {}", request.getIdentifier());

        // 1. Validate uniqueness
        validateUniqueFields(request);

        // 2. Map and save patient
        Patient patient = new Patient();
        patient.setIdentifier(request.getIdentifier());
        patient.setUsername(request.getUsername());
        patient.setEmail(request.getEmail());
        patient.setGender(request.getGender());
        patient.setCreatedAt(LocalDateTime.now());
        patient.setUpdatedAt(LocalDateTime.now());
        patient.setGivenName(request.getGivenName());
        patient.setFamilyName(request.getFamilyName());
        patient.setBirthDate(request.getBirthDate());

        patient = patientRepository.saveAndFlush(patient);

        // Initialize collections
        if (patient.getEncounters() == null) {
            patient.setEncounters(new ArrayList<>());
        }
        if (patient.getObservations() == null) {
            patient.setObservations(new ArrayList<>());
        }

        // 3. Handle encounters
        if (request.getEncounters() != null) {
            for (PatientRequest.EncounterRequest encReq : request.getEncounters()) {
                Encounter encounter = new Encounter();
                encounter.setPatient(patient);
                encounter.setStart(encReq.getStart());
                encounter.setEndTime(encReq.getEndTime());
                encounter.setEncounterClass(Encounter.EncounterClass.valueOf(encReq.getEncounterClass().toUpperCase()));

                // Initialize observations collection
                if (encounter.getObservations() == null) {
                    encounter.setObservations(new ArrayList<>());
                }

                encounter = encounterRepository.saveAndFlush(encounter);

                // Handle observations for this encounter
                if (encReq.getObservations() != null) {
                    for (PatientRequest.ObservationRequest obsReq : encReq.getObservations()) {
                        Observation obs = new Observation();
                        obs.setPatient(patient);
                        obs.setEncounter(encounter);
                        obs.setCode(obsReq.getCode());
                        obs.setValue(obsReq.getValue());
                        obs.setEffectiveDateTime(obsReq.getEffectiveDateTime());

                        obs = observationRepository.saveAndFlush(obs);

                        // Add observation to encounter in-place
                        encounter.getObservations().add(obs);
                    }
                }

                // Add encounter to patient in-place
                patient.getEncounters().add(encounter);
            }
        }

        // 4. Handle direct patient observations
        if (request.getObservations() != null) {
            for (PatientRequest.ObservationRequest obsReq : request.getObservations()) {
                Observation obs = new Observation();
                obs.setPatient(patient);
                obs.setCode(obsReq.getCode());
                obs.setValue(obsReq.getValue());
                obs.setEffectiveDateTime(obsReq.getEffectiveDateTime());

                obs = observationRepository.saveAndFlush(obs);

                // Add to patient in-place
                patient.getObservations().add(obs);
            }
        }

        log.info("Created patient with ID: {}", patient.getId());
        return patientMapper.toResponse(patient);
    }


    @Override
    @Transactional
    public PatientResponse updatePatient(UUID id, PatientRequest request) {
        log.info("Updating patient with ID: {}", id);
        
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        
        if(request != null) {
            if (request.getIdentifier() != null && !request.getIdentifier().isBlank()) {
                if (!existingPatient.getIdentifier().equals(request.getIdentifier()) &&
                        patientRepository.existsByIdentifier(request.getIdentifier())) {
                    throw new IllegalArgumentException("Patient with identifier " + request.getIdentifier() + " already exists");
                }
                existingPatient.setIdentifier(request.getIdentifier());
            }

            if (request.getGivenName() != null && !request.getGivenName().isBlank()) {
                existingPatient.setGivenName(request.getGivenName());
            }

            if (request.getFamilyName() != null && !request.getFamilyName().isBlank()) {
                existingPatient.setFamilyName(request.getFamilyName());
            }

            if (request.getBirthDate() != null) {
                existingPatient.setBirthDate(request.getBirthDate());
            }

            if (request.getUsername() != null && !request.getUsername().isBlank()) {
                existingPatient.setUsername(request.getUsername());
            }

            if (request.getEmail() != null && !request.getEmail().isBlank()) {
                existingPatient.setEmail(request.getEmail());
            }

            if (request.getGender() != null) {
                existingPatient.setGender(request.getGender());
            }
        }
        Patient updatedPatient = patientRepository.save(existingPatient);
        log.info("Updated patient with ID: {}", id);
        return patientMapper.toResponse(updatedPatient);
    }

    @Override
    @Transactional
    public void deletePatient(UUID id) {
        log.info("Deleting patient with ID: {}", id);
        
        if (!patientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Patient not found with id: " + id);
        }
        
        patientRepository.deleteById(id);
        log.info("Deleted patient with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PatientResponse> searchPatients(String familyName, String givenName, String identifier, LocalDate birthDate, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        
        log.debug("Searching patients with filters - familyName: {}, givenName: {}, identifier: {}, birthDate: {}, startDate: {}, endDate: {}",
                familyName, givenName, identifier, birthDate, startDate, endDate);
        
        return patientRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (StringUtils.hasText(familyName)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("familyName")),
                    "%" + familyName.toLowerCase() + "%"
                ));
            }
            
            if (StringUtils.hasText(givenName)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("givenName")),
                    "%" + givenName.toLowerCase() + "%"
                ));
            }
            
            if (StringUtils.hasText(identifier)) {
                predicates.add(criteriaBuilder.equal(root.get("identifier"), identifier));
            }
            
            if (birthDate != null) {
                predicates.add(criteriaBuilder.equal(root.get("birthDate"), birthDate));
            } else if (startDate != null || endDate != null) {
                if (startDate != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("birthDate"), startDate));
                }
                if (endDate != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("birthDate"), endDate));
                }
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable).map(patientMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Encounter> getPatientEncounters(UUID patientId, Pageable pageable) {
        log.debug("Fetching encounters for patient ID: {}", patientId);
        
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }
        
        return encounterRepository.findByPatientId(patientId, pageable);
    }

    @Override
    public Patient getPatientEntity(Long id) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public Patient getPatientEntity(UUID id) {
        log.debug("Fetching patient entity with ID: {}", id);
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
    }
}
