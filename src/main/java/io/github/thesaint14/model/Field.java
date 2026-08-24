package io.github.thesaint14.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Field {
    private final String name;
    private final FieldType type;
}
