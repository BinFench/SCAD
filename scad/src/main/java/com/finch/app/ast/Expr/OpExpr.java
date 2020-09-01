package com.finch.app;

public class OpExpr extends Expr {
    public String name;
    public Argument argument;

    OpExpr(String name, Argument argument) {
        this.id = "Expr";
        this.exprID = "Op";
        this.name = name;
        this.argument = argument;
    }

    public String prettyPrint(String temp) {
        return this.name + "(" + this.argument.prettyPrint("") + ")" + temp;
    }
}