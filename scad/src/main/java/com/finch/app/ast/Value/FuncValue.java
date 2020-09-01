package com.finch.app;

public class FuncValue extends Value {
    public FuncCallExpr expr;
    FuncValue(String name, Argument argument) {
        this.id = "Value";
        this.valID = "Func";
        this.expr = new FuncCallExpr(name, argument);
    }

    public String prettyPrint(String temp) {
        return this.expr.prettyPrint(temp);
    }
}