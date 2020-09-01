package com.finch.app;

public class OpValue extends Value {
    public Expr expr;
    OpValue(Expr expr) {
        this.id = "Value";
        this.valID = "Op";
        this.expr = expr;
    }

    public String prettyPrint(String temp, Boolean label) {
        System.out.println("OPVALUE[");
        String toPrint = (label ? "[OPVALUE]" : "") + this.expr.prettyPrint(temp, label);
        System.out.println("]OPVALUE");
        return toPrint;
    }
}