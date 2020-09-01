package com.finch.app;

public class FloatValue extends Value {
    public double value;

    FloatValue(double value) {
        this.valID = "Float";
        this.id = "Value";
        this.value = value;
    }

    public String prettyPrint(String temp, Boolean label) {
        System.out.println("FLOAT[");
        String toPrint = (label ? "[FLOAT]" : "") + "" + this.value + temp;
        System.out.println("]FLOAT");
        return toPrint;
    }
}