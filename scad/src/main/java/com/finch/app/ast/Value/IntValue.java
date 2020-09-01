package com.finch.app;

public class IntValue extends Value {
    public int value;

    IntValue(int value) {
        this.valID = "Int";
        this.id = "Value";
        this.value = value;
    }

    public String prettyPrint(String temp) {
        return "" + this.value + temp;
    }
}