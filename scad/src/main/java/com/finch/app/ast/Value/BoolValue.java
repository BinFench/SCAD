package com.finch.app;

public class BoolValue extends Value {
    public Boolean value;

    BoolValue(Boolean value) {
        this.id = "Bool";
        this.value = value;
    }
}