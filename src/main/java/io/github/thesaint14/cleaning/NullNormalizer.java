// io/github/thesaint14/cleaning/NullNormalizer.java
package io.github.thesaint14.cleaning;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.github.thesaint14.model.Field;
import io.github.thesaint14.model.FieldType;
import io.github.thesaint14.model.Record;
import io.github.thesaint14.model.Schema;
import io.github.thesaint14.parser.ParsedData;

public class NullNormalizer {

    public ParsedData normalize(ParsedData data) {
        List<Record> cleaned = data.getRecords().stream()
                .map(record -> normalizeRecord(data.getSchema(), record))
                .collect(Collectors.toList());
        return new ParsedData(data.getSchema(), cleaned);
    }

    private Record normalizeRecord(Schema schema, Record record) {
        Map<String, Object> values = new HashMap<>(record.getValues());
        for (Field field : schema.getFields()) {
            if (field.getType() != FieldType.STRING && isBlank(values.get(field.getName()))) {
                values.put(field.getName(), null);
            }
        }
        return new Record(values, record.getPaddedFields());
    }

    private boolean isBlank(Object value) {
        return value == null || "".equals(value);
    }
}