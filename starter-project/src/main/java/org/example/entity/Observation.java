package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "observations")
public class Observation extends BaseEntity {

    @NotNull(message = "Patient is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encounter_id")
    private Encounter encounter;

    @NotNull(message = "Code is required")
    @Column(nullable = false, length = 100)
    private String code;

    @NotNull(message = "Value is required")
    @Column(nullable = false, length = 255)
    private String value;

    @NotNull(message = "Effective date/time is required")
    @Column(name = "effective_date_time", nullable = false)
    private LocalDateTime effectiveDateTime;


    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Encounter getEncounter() {
        return encounter;
    }

    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalDateTime getEffectiveDateTime() {
        return effectiveDateTime;
    }

    public void setEffectiveDateTime(LocalDateTime effectiveDateTime) {
        this.effectiveDateTime = effectiveDateTime;
    }
}
