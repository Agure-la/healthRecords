package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ApiResponse;
import org.example.dto.ObservationResponse;
import org.example.dto.PatientRequest;
import org.example.dto.PatientResponse;
import org.example.entity.Encounter;
import org.example.service.ObservationService;
import org.example.service.PatientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patients", description = "Endpoints for managing patients, encounters, and observations")
@RequiredArgsConstructor
public class PatientController {

    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_PAGE_SIZE = "10";
    private static final String DEFAULT_SORT_BY = "familyName,asc";

    private final PatientService patientService;

    private final ObservationService observationService;

    @Operation(summary = "Create a new patient", description = "Registers a new patient along with optional encounters and observations.")
    @PostMapping
    public ResponseEntity<ApiResponse<PatientResponse>> createPatient(@Valid @RequestBody PatientRequest request) {
        log.info("Creating new patient with identifier: {}", request.getIdentifier());
        PatientResponse patient = patientService.createPatient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Patient created successfully", patient));
    }

    @Operation(summary = "Fetch patient by ID", description = "Retrieves patient details along with associated information.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatientById(@PathVariable UUID id) {
        log.info("Fetching patient with ID: {}", id);
        PatientResponse patient = patientService.getPatientById(id);
        return ResponseEntity.ok(ApiResponse.success("Patient retrieved successfully", patient));
    }

    @Operation(summary = "Update patient information", description = "Updates an existing patient's demographic and clinical information.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponse>> updatePatient(@PathVariable UUID id, @Valid @RequestBody PatientRequest request) {
        log.info("Updating patient with ID: {}", id);
        PatientResponse updatedPatient = patientService.updatePatient(id, request);
        return ResponseEntity.ok(ApiResponse.success("Patient updated successfully", updatedPatient));
    }

    @Operation(summary = "Delete a patient", description = "Deletes a patient record and all associated encounters and observations.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePatient(@PathVariable UUID id) {
        log.info("Deleting patient with ID: {}", id);
        patientService.deletePatient(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success("Patient deleted successfully", null));
    }

    @Operation(summary = "Search patients", description = "Search patients using filters such as name, identifier, or date of birth.")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PatientResponse>>> searchPatients(@RequestParam(required = false) String family, @RequestParam(required = false) String given,
            @RequestParam(required = false) String identifier, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page, @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size, @RequestParam(defaultValue = DEFAULT_SORT_BY) String[] sort) {

        log.info("Searching patients with filters - family: {}, given: {}, identifier: {}, birthDate: {}, startDate: {}, endDate: {}, page: {}, size: {}", family, given, identifier, birthDate, startDate, endDate, page, size);
        Pageable pageable = PageRequest.of(page, size, parseSort(sort));
        Page<PatientResponse> patients = patientService.searchPatients(family, given, identifier, birthDate, startDate, endDate, pageable);

        return ResponseEntity.ok(ApiResponse.success("Patient search successful", patients));
    }

    @Operation(summary = "Get encounters for a patient", description = "Retrieves all encounters for the given patient ID with pagination.")
    @GetMapping("/{id}/encounters")
    public ResponseEntity<ApiResponse<Page<Encounter>>> getPatientEncounters(@PathVariable UUID id, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size) {
        log.info("Fetching encounters for patient ID: {}", id);
        Pageable pageable = PageRequest.of(page, size, Sort.by("start").descending());
        Page<Encounter> encounters = patientService.getPatientEncounters(id, pageable);

        return ResponseEntity.ok(ApiResponse.success("Encounters retrieved successfully", encounters));
    }

    private Sort parseSort(String[] sort) {
        if (sort == null || sort.length == 0) {
            return Sort.by("familyName").ascending();
        }

        String[] sortParams = sort[0].split(",");
        if (sortParams.length == 2) {
            String property = sortParams[0];
            String direction = sortParams[1];
            return Sort.by(Sort.Direction.fromString(direction), property);
        }

        return Sort.by("familyName").ascending();
    }

    @Operation(summary = "Get observations for a patient", description = "Fetches all observations recorded for a specific patient.")
    @GetMapping("/{id}/observations")
    public ResponseEntity<ApiResponse<List<ObservationResponse>>> getPatientObservations(@PathVariable("id") UUID patientId) {
        log.info("GET /api/patients/{}/observations called", patientId);

        try {
            List<ObservationResponse> observations = observationService.getObservationsByPatientId(patientId);

            if (observations.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.success("No observations found for this patient", observations));
            }

            return ResponseEntity.ok(ApiResponse.success("Observations retrieved successfully", observations));

        } catch (Exception ex) {
            log.error("Error retrieving observations for patient ID: {}", patientId, ex);
            return ResponseEntity
                    .internalServerError()
                    .body(ApiResponse.error("An unexpected error occurred while retrieving observations"));
        }
    }
}
