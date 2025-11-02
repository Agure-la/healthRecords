package org.example.specification;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;

@Data
@AllArgsConstructor
public class SearchCriteria {
    private String key;
    private SearchOperation operation;
    private Object value;

    public static SearchCriteria like(String key, String value) {
        return new SearchCriteria(key, SearchOperation.MATCH, value);
    }

    public static SearchCriteria equal(String key, Object value) {
        return new SearchCriteria(key, SearchOperation.EQUAL, value);
    }

    public static SearchCriteria dateEqual(String key, Object value) {
        return new SearchCriteria(key, SearchOperation.DATE_EQUAL, value);
    }

    public static SearchCriteria dateBetween(String key, Object from, Object to) {
        return new SearchCriteria(key, SearchOperation.DATE_BETWEEN, Arrays.asList(from, to));
    }
}
