package org.example.repository;

import org.example.entity.Encounter;
import org.example.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EncounterRepository extends JpaRepository<Encounter, Long> {

    List<Encounter> findByPatientIdOrderByStartDesc(UUID patientId);

    /**
     * Find all encounters for a specific patient with pagination
     * @param patientId The ID of the patient
     * @param pageable Pagination information
     * @return Page of encounters
     */
    Page<Encounter> findByPatientId(UUID patientId, Pageable pageable);

    @Query("""
        SELECT e FROM Encounter e 
        WHERE e.patient = :patient 
        AND (e.start BETWEEN :startDate AND :endDate 
             OR e.endTime BETWEEN :startDate AND :endDate)
    """)
    List<Encounter> findEncountersByPatientAndDateRange(
            @Param("patient") Patient patient,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
        SELECT COUNT(e) > 0 
        FROM Encounter e 
        WHERE e.patient = :patient 
        AND e.start <= :endDate 
        AND (e.endTime IS NULL OR e.endTime >= :startDate)
    """)
    boolean hasOverlappingEncounters(
            @Param("patient") Patient patient,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
