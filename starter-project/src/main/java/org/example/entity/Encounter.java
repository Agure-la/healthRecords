package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "encounters",
    indexes = {
        @Index(name = "idx_encounter_patient_id", columnList = "patient_id"),
        @Index(name = "idx_encounter_period", columnList = "start, end")
    })
@Getter
@Setter
public class Encounter {

    @Id
    @GeneratedValue
    private UUID id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private Long version;

   // @NotNull(message = "Patient is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotNull(message = "Start date/time is required")
    @Column(nullable = false)
    private LocalDateTime start;

    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EncounterClass encounterClass;

    @OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Observation> observations = new ArrayList<>();

    public enum EncounterClass {
        OUTPATIENT, INPATIENT, EMERGENCY, AMBULATORY, VIRTUAL
    }

    @Override
    public String toString() {
        return "Encounter{" +
                "id=" + getId() +
                ", patientId=" + (patient != null ? patient.getId() : null) +
                ", start=" + start +
                ", end=" + endTime +
                ", encounterClass=" + encounterClass +
                '}';
    }
}
