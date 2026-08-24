package io.github.thesaint14.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Schema {
    private final String name;
    private final List<Field> fields;
}
