package com.finch.app;

public class Var extends Scope {
    public String type;
    public String name;
    public Expr value;
    public Boolean assigned;

    Var(String type, String name) {
        if (name == "true" || name == "false") {
            throw new IllegalParsableException("Illegal assignment: Cannot assign reserved word as variable name.");
        }
        this.type = type;
        this.name = name;
        this.id = "Var";
        this.assigned = false;
    }

    public Var assign(Expr value) {
        this.value = value;
        this.assigned = true;
        return this;
    }

    public String prettyPrint(String temp) {
        String toPrint = type + " " + name;
        if (this.assigned) {
            toPrint += " = " + this.value.prettyPrint(temp);
        }

        return toPrint + temp;
    }

    public String prettyPrint(Boolean a) {
        String toPrint = type + " " + name;
        if (this.assigned) {
            throw new IllegalParsableException("Illegal scope: current scope does not allow variable assignment.");
        }

        return toPrint;
    }
}