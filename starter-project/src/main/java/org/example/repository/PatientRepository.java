package org.example.repository;

import org.example.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID>, JpaSpecificationExecutor<Patient> {
    
    Optional<Patient> findByIdentifier(String identifier);
    
    List<Patient> findByFamilyNameContainingIgnoreCaseAndGivenNameContainingIgnoreCase(
            String familyName, String givenName);
            
    List<Patient> findByBirthDate(LocalDate birthDate);
    
    List<Patient> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);
    
    boolean existsByIdentifier(String identifier);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
