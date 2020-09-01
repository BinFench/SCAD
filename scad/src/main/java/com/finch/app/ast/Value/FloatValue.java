package com.finch.app;

public class FloatValue extends Value {
    public double value;

    FloatValue(double value) {
        this.valID = "Float";
        this.id = "Value";
        this.value = value;
    }

    public String prettyPrint(String temp) {
        return "" + this.value + temp;
    }
}