package io.github.thesaint14.model;

public class Field {
    private String name;
    private FieldType type;

    public Field(String name, FieldType type){
        this.name = name;
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public FieldType getType() {
        return type;
    }
}
