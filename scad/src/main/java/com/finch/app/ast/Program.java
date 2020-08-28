package com.finch.app;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Program {
    public List<Scope> scopes;
    public List<Func> funcs;
    public List<Class> classes;
    public List<Var> vars;
    public List<Expr> exprs;

    Program() {
        this.scopes = new ArrayList<Scope>();
    }

    public Program addScope(Scope scope) {
        this.scopes.add(scope);
        return this;
    }

    public Scope getScope(int i) {
        return this.scopes.get(i);
    }
}