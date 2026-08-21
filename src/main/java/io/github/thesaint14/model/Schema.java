package io.github.thesaint14.model;
import java.util.List;

public class Schema {
    private String name;
    private List<Field> fields;
    
    public Schema(String name, List<Field> fields){
        this.name = name;
        this.fields = fields;
    }

    public String getName() {
        return name;
    }
    public List<Field> getFields() {
        return fields;
    }

}
