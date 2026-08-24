package io.github.thesaint14.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Record {
    private final Map<String, Object> values;

    public Object getValue(String fieldName) {
        return values.get(fieldName);
    }
}
