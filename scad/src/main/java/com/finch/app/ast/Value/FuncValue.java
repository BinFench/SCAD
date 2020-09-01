package com.finch.app;

public class FuncValue extends Value {
    public Expr expr;
    FuncValue(String name, Argument argument) {
        this.id = "Value";
        this.valID = "Func";
        this.expr = new FuncCallExpr(name, argument);
    }

    public String prettyPrint(String temp, Boolean label) {
        System.out.println("FUNCVALUE[");
        String toPrint = (label ? "[FUNCVALUE]" : "") + this.expr.prettyPrint(temp, label);
        System.out.println("]FUNCVALUE");
        return toPrint;
    }
}