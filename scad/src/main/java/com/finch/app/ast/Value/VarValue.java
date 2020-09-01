package com.finch.app;

public class VarValue extends Value {
    public String name;

    VarValue(String name) {
        this.valID = "Var";
        this.id = "Value";
        this.name = name;
    }

    public String prettyPrint(String temp, Boolean label) {
        System.out.println("VARVALUE[");
        String toPrint = (label ? "[VARVALUE]" : "") + this.name + temp;
        System.out.println("]VARVALUE");
        return toPrint;
    }
}