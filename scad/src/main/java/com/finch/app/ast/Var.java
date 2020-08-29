package com.finch.app;

public class Var extends Scope {
    public String type;
    public String name;
    public Expr value;

    Var(String type, String name) {
        if (name == "true" || name == "false") {
            throw new IllegalParsableException("Illegal assignment: Cannot assign reserved word as variable name.");
        }
        this.type = type;
        this.name = name;
        this.id = "Var";
    }

    public Var assign(Expr value) {
        this.value = value;
        return this;
    }
}