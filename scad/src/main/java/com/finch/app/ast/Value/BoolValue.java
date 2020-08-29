package com.finch.app;

public class BoolValue extends Value {
    public Boolean value;

    BoolValue(Boolean value) {
        this.valID = "Bool";
        this.id = "Value";
        this.value = value;
    }
}