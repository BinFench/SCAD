package com.finch.app;

public class StringValue extends Value {
    public String value;
    public Argument arguments;
    public Boolean hasArg;

    StringValue(String value) {
        this.valID = "String";
        this.id = "Value";
        this.value = value;
        this.hasArg = false;
    }

    public StringValue addArg(Argument arg) {
        this.arguments = arg;
        this.hasArg = true;
        return this;
    }
}