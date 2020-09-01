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

    public String prettyPrint(String temp, Boolean label) {
        System.out.println("STRING[");
        String toPrint = (label ? "[STRING]" : "") + this.value;
        if (this.hasArg) {
            toPrint += " % " + this.arguments.prettyPrint("", label);
        }
        System.out.println("STRING[");
        return toPrint + temp;
    }
}