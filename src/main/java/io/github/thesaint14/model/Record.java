package io.github.thesaint14.model;

import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Record {
    private final Map<String, Object> values;
    private final Set<String> paddedFields;

    public Object getValue(String fieldName) {
        return values.get(fieldName);
    }
}
