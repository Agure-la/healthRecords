package org.example.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Base specification for building JPA specifications
 * @param <T> The entity type
 */
@Data
@AllArgsConstructor
public class BaseSpecification<T> implements Specification<T> {

    private final List<SearchCriteria> criteriaList;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        for (SearchCriteria criteria : criteriaList) {
            if (criteria.getOperation().equals(SearchOperation.MATCH)) {
                predicates.add(builder.like(
                        builder.lower(root.get(criteria.getKey())),
                        "%" + criteria.getValue().toString().toLowerCase() + "%"
                ));
            } else if (criteria.getOperation().equals(SearchOperation.EQUAL)) {
                predicates.add(builder.equal(
                        root.get(criteria.getKey()),
                        criteria.getValue()
                ));
            } else if (criteria.getOperation().equals(SearchOperation.DATE_EQUAL)) {
                predicates.add(builder.equal(
                        root.get(criteria.getKey()),
                        criteria.getValue()
                ));
            } else if (criteria.getOperation().equals(SearchOperation.DATE_BETWEEN)) {
                @SuppressWarnings("unchecked")
                List<Comparable<Object>> dates = (List<Comparable<Object>>) criteria.getValue();
                predicates.add(builder.between(
                        root.get(criteria.getKey()),
                        Objects.requireNonNull(dates.get(0)),
                        Objects.requireNonNull(dates.get(1))
                ));
            }
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
