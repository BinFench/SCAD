package com.finch.app;

public class Scope {
    String id;
    String origString;
   
    public Scope addString(String og) {
        this.origString = og;
        return this;
    }
}