package com.finch.app;

public class FloatValue extends Value {
    public double value;

    FloatValue(double value) {
        this.id = "Float";
        this.value = value;
    }
}