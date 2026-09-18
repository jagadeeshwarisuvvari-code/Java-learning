package io.github.thesaint14.db;

final class SqlIdentifiers {

    private SqlIdentifiers() {}

    static String validate(String name){
        if (!name.matches("[A-Za-z_][A-Za-z0-9_]")){
            throw new IllegalArguementException("Unsafe SQL identifier: " + name);
        }
        return name;
    }
    
}
