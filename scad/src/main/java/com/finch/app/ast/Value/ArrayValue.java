package com.finch.app;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class ArrayValue extends Value {
    public List<Value> values;
    public String type;

    ArrayValue(String type) {
        this.type = type;
        this.id = "Value";
        this.valID = "Array";
        this.values = new ArrayList<Value>();
    }

    public ArrayValue addValue(Value value) {
        if(value instanceof ArrayValue && this.type != ((ArrayValue)value).type) {
            throw new IllegalParsableException("Type mismatch: Array item types don't match");
        } else {
            if (this.type != value.valID) {
                throw new IllegalParsableException("Type mismatch: Array item types don't match");
            }
            this.values.add(value);
        }
        return this;
    }

    public Value getValue(int i) {
        return this.values.get(i);
    }

    public String prettyPrint(String temp) {
        String toPrint = "[";
        for (int i = 0; i < this.values.size(); i++) {
            toPrint += getValue(i).prettyPrint("");
            if (i < this.values.size() - 1) {
                toPrint += ", ";
            }
        }
        return toPrint + "]" + temp;
    }
}