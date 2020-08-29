package com.finch.app;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Class extends Scope {
    public String name;
    public List<Scope> scopes;
    public List<Func> funcs;
    public List<Var> vars;

    Class(String name) {
        this.name = name;
        this.scopes = new ArrayList<Scope>();
        this.id = "Class";
    }

    public Class addScope(Scope scope) {
        if (scope.id == "Var" || scope.id == "Func") {
            this.scopes.add(scope);
        } else {
            throw new IllegalParsableException("Statements within class must be encapsulated in functions.");
        }
        return this;
    }

    public Scope getScope(int i) {
        return this.scopes.get(i);
    }
}