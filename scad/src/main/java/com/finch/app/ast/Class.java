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

    public Var getVar(int i) {
        return this.vars.get(i);
    }

    public Func getFunc(int i) {
        return this.funcs.get(i);
    }

    public String prettyPrint(String temp, Boolean label) {
        System.out.println("CLASS[");
        this.vars = new ArrayList<Var>();
        this.funcs = new ArrayList<Func>();

        String type = "Start";
        String toPrint = (label ? "[CLASS]" : "") + name + " {\n";

        for (int i = 0; i < this.scopes.size(); i++) {
            if (getScope(i) instanceof Func) {
                this.funcs.add((Func)getScope(i));
            } else if (getScope(i) instanceof Var) {
                this.vars.add((Var)getScope(i));
            } else {
                throw new IllegalParsableException("Illegal scope: Class scope can only contain Functions, and Vars.");
            }
        }

        for (int i = 0; i < this.vars.size(); i++) {
            type = "Var";
            toPrint += "\t" + getVar(i).prettyPrint(true, label) + ";";
            if (i < this.vars.size() - 1) {
                toPrint += "\n";
            }
        }

        for (int i = 0; i < this.funcs.size(); i++) {
            if (type != "Start" && type != "Func") {
                toPrint += "\n\n";
            }
            type = "Func";
            toPrint += "\t" + format(getFunc(i).prettyPrint("", label));
            if (i < this.funcs.size() - 1) {
                toPrint += "\n\n";
            }
        }

        System.out.println("]CLASS");
        return toPrint + "\n}";
    }
}