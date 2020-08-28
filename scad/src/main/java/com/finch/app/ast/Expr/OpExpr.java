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
}