package com.finch.app;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Func extends Scope {
    public String type;
    public String name;
    public List<Scope> scopes;
    public List<Var> vars;
    public List<Expr> exprs;
    public Argument arguments;

    Func(String type, String name, Argument arguments) {
        this.type = type;
        this.name = name;
        this.scopes = new ArrayList<Scope>();
        this.id = "Func";
        this.arguments = arguments;
    }

    public void addScope(Scope scope) {
        if (scope.id != "Func" && scope.id != "Class") {
            this.scopes.add(scope);
        } else {
            throw new IllegalParsableException("Cannot declare function or class within function");
        }
    }

    public Scope getScope(int i) {
        return this.scopes.get(i);
    }
}