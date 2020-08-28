package com.finch.app;

public class StringValue extends Value {
    public String value;

    StringValue(String value) {
        this.id = "String";
        this.value = value;
    }
}