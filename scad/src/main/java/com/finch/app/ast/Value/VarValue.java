package com.finch.app;

public class VarValue extends Value {
    public String name;

    VarValue(String name) {
        this.valID = "Var";
        this.id = "Value";
        this.name = name;
    }

    public String prettyPrint(String temp) {
        return this.name + temp;
    }
}