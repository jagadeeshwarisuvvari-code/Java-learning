package io.github.thesaint14.model;

import java.util.Map;

public class Record {
    private Map<String, Object> values;

    public Record(Map<String, Object> values) {
        this.values = values;
    }
    public Object getValue(String fieldName) {
        return values.get(fieldName);
    }

    public Map<String, Object > getValues() {
        return values;
    }
}
