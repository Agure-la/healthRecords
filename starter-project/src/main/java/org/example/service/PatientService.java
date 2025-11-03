package org.example.service;

import org.example.dto.EncounterResponse;
import org.example.dto.PatientRequest;
import org.example.dto.PatientResponse;
import org.example.exception.ResourceNotFoundException;
import org.example.entity.Encounter;
import org.example.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Service interface for managing patients.
 */
public interface PatientService {
    
    /**
     * Create a new patient.
     * @param request Patient data
     * @return Created patient
     * @throws IllegalArgumentException if a patient with the same identifier already exists
     */
    PatientResponse createPatient(PatientRequest request);
    
    /**
     * Get a patient by ID.
     * @param id Patient ID
     * @return Patient data
     * @throws ResourceNotFoundException if patient is not found
     */
    PatientResponse getPatientById(UUID id);
    
    /**
     * Update an existing patient.
     * @param id Patient ID
     * @param request Updated patient data
     * @return Updated patient
     * @throws ResourceNotFoundException if patient is not found
     * @throws IllegalArgumentException if the new identifier is already in use
     */
    PatientResponse updatePatient(UUID id, PatientRequest request);
    
    /**
     * Delete a patient by ID.
     * @param id Patient ID
     * @throws ResourceNotFoundException if patient is not found
     */
    void deletePatient(UUID id);
    
    /**
     * Search patients by various criteria.
     * All parameters are optional. If a parameter is null, it will be ignored in the search.
     * 
     * @param familyName Full or partial family name (case-insensitive)
     * @param givenName Full or partial given name (case-insensitive)
     * @param identifier Exact identifier match
     * @param birthDate Exact birth date match
     * @param startDate Start of date range (inclusive)
     * @param endDate End of date range (inclusive)
     * @param pageable Pagination and sorting information
     * @return Page of matching patients
     */
    Page<PatientResponse> searchPatients(
            String familyName,
            String givenName,
            String identifier,
            LocalDate birthDate,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );
    
    /**
     * Get all encounters for a specific patient.
     * @param patientId The ID of the patient
     * @param pageable Pagination information
     * @return Page of encounters
     * @throws ResourceNotFoundException if patient is not found
     */
    Page<EncounterResponse> getPatientEncounters(UUID patientId, Pageable pageable);
    
    /**
     * Get patient entity by ID (for internal use).
     * @param id Patient ID
     * @return Patient entity
     * @throws ResourceNotFoundException if patient is not found
     */
    Patient getPatientEntity(Long id);

    @Transactional(readOnly = true)
    Patient getPatientEntity(UUID id);
}
