package org.example.mapper;

import org.example.dto.PatientRequest;
import org.example.dto.PatientResponse;
import org.example.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for converting between Patient entity and DTOs
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PatientMapper {
    
    /**
     * Convert PatientRequest to Patient entity
     * @param request PatientRequest DTO
     * @return Patient entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Patient toEntity(PatientRequest request);
    
    /**
     * Update Patient entity from PatientRequest DTO
     * @param request PatientRequest DTO with updated data
     * @param patient Patient entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "version", ignore = true)
    void updatePatientFromRequest(PatientRequest request, @MappingTarget Patient patient);
    
    /**
     * Update Patient entity from PatientRequest
     * @param request PatientRequest DTO
     * @param entity Patient entity to update
     */
    void updateEntity(PatientRequest request, @MappingTarget Patient entity);
    
    /**
     * Convert Patient entity to PatientResponse
     * @param patient Patient entity
     * @return PatientResponse DTO
     */
    PatientResponse toResponse(Patient patient);
    
    /**
     * Update PatientResponse from Patient entity
     * @param patient Patient entity
     * @param response PatientResponse to update
     */
    @Mapping(target = "id", source = "patient.id")
    @Mapping(target = "identifier", source = "patient.identifier")
    @Mapping(target = "givenName", source = "patient.givenName")
    @Mapping(target = "familyName", source = "patient.familyName")
    @Mapping(target = "birthDate", source = "patient.birthDate")
    @Mapping(target = "gender", source = "patient.gender")
    @Mapping(target = "createdAt", source = "patient.createdAt")
    @Mapping(target = "updatedAt", source = "patient.updatedAt")
    void updateResponse(Patient patient, @MappingTarget PatientResponse response);
}
