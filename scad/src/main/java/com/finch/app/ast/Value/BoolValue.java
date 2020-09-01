package com.finch.app;

public class BoolValue extends Value {
    public Boolean value;

    BoolValue(Boolean value) {
        this.valID = "Bool";
        this.id = "Value";
        this.value = value;
    }

    public String prettyPrint(String temp, Boolean label) {
        System.out.println("BOOL[");
        String toPrint;
        if (this.value) {
            toPrint = (label ? "[BOOL]" : "") + "true" + temp;
        } else {
            toPrint = (label ? "[BOOL]" : "") + "false" + temp;
        }
        System.out.println("]BOOL");
        return toPrint;
    }
}