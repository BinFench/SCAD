package com.finch.app;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class ArrayValue extends Value {
    public List<Value> values;
    public String type;

    ArrayValue(String type) {
        this.type = type;
        this.id = "Array";
        this.values = new ArrayList<Value>();
    }

    public ArrayValue addValue(Value value) {
        if (this.type == value.id) {
            this.values.add(value);
        } else {
            throw new IllegalParsableException("Type mismatch: Array item types don't match");
        }
        return this;
    }
}