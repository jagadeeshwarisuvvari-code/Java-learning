package io.github.thesaint14.parser;

import java.util.List;

import io.github.thesaint14.model.Record;
import io.github.thesaint14.model.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParsedData {
    private final Schema schema;
    private final List<Record> records;    
}
