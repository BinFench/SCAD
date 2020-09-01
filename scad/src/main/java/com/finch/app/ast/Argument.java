package com.finch.app;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Argument extends CommonNode {
    public int valType;
    public List<Var> vars;
    public List<Expr> terms;

    Argument() {
        this.valType = 0;
        this.vars = new ArrayList<Var>();
        this.terms = new ArrayList<Expr>();
    }

    public Argument addVar(Var var) {
        if (this.valType != 2) {
            this.valType = 1;
            this.vars.add(var);
            return this;
        } else {
            throw new IllegalParsableException("Argument mismatch: cannot have definitions and passed variables in same Argument set.");
        }
    }

    public Argument addVal(Expr val) {
        if (this.valType != 1) {
            this.valType = 2;
            this.terms.add(val);
            return this;
        } else {
            throw new IllegalParsableException("Argument mismatch: cannot have definitions and passed variables in same Argument set.");
        }
    }

    public Var getVar(int i) {
        return this.vars.get(i);
    }

    public Expr getVal(int i) {
        return this.terms.get(i);
    }

    public String prettyPrint(String temp, Boolean label) {
        System.out.println("ARGUMENT[");
        String toPrint = label ? "[ARGUMENT]" : "";
        if (this.valType == 1) {
            for (int i = 0; i < this.vars.size(); i++) {
                toPrint += getVar(i).prettyPrint(true, label);
                if (i < this.vars.size() - 1) {
                    toPrint += ", ";
                }
            }
        } else if (this.valType == 2) {
            for (int i = 0; i < this.terms.size(); i++) {
                toPrint += getVal(i).prettyPrint("", label);
                if (i < this.terms.size() - 1) {
                    toPrint += ", ";
                }
            }
        }
        System.out.println("]ARGUMENT");
        return toPrint;
    }
}