package org.example.repository;

import org.example.entity.Observation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ObservationRepository extends JpaRepository<Observation, UUID> {
    List<Observation> findByPatientIdOrderByEffectiveDateTimeDesc(UUID patientId);
}
