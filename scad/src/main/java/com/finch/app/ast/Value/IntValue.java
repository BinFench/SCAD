package com.finch.app;

public class IntValue extends Value {
    public int value;

    IntValue(int value) {
        this.valID = "Int";
        this.id = "Value";
        this.value = value;
    }

    public String prettyPrint(String temp, Boolean label) {
        System.out.println("INT[");
        String toPrint = (label ? "[INT]" : "") + this.value + temp;
        System.out.println("]INT");
        return toPrint;
    }
}