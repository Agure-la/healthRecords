package org.example.specification;

import jakarta.persistence.criteria.Predicate;
import org.example.entity.Patient;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Utility class for building Patient specifications
 */
public class PatientSpecifications {

    private PatientSpecifications() {
    }

    public static Specification<Patient> hasFamilyNameLike(String familyName) {
        return (root, query, criteriaBuilder) -> {
            if (familyName == null || familyName.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("familyName")),
                    "%" + familyName.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Patient> hasGivenNameLike(String givenName) {
        return (root, query, criteriaBuilder) -> {
            if (givenName == null || givenName.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("givenName")),
                    "%" + givenName.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Patient> hasIdentifier(String identifier) {
        return (root, query, criteriaBuilder) -> {
            if (identifier == null || identifier.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("identifier"), identifier);
        };
    }

    public static Specification<Patient> hasBirthDate(LocalDate birthDate) {
        return (root, query, criteriaBuilder) -> {
            if (birthDate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("birthDate"), birthDate);
        };
    }

    public static Specification<Patient> hasBirthDateBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null || endDate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.between(root.get("birthDate"), startDate, endDate);
        };
    }

    @SafeVarargs
    public static Specification<Patient> withSpecifications(Specification<Patient>... specs) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (specs != null) {
                Arrays.stream(specs)
                    .filter(Objects::nonNull)
                    .map(spec -> spec.toPredicate(root, query, criteriaBuilder))
                    .filter(Objects::nonNull)
                    .forEach(predicates::add);
            }
            return predicates.isEmpty() ? criteriaBuilder.conjunction() : 
                   criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
