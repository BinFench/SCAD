package com.finch.app;

public class Var extends Scope {
    public String type;
    public String name;
    public Value value;

    Var(String type, String name) {
        this.type = type;
        this.name = name;
        this.id = "Var";
    }

    public void assign(Value value) {
        this.value = value;
    }
}