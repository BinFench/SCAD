package com.finch.app;

public class FuncCallExpr extends Expr {
    public String name;
    public Argument arguments;

    FuncCallExpr(String name, Argument arguments) {
        this.id = "Expr";
        this.exprID = "FuncCall";
        this.name = name;
        this.arguments = arguments;
    }

    public String prettyPrint(String temp) {
        return this.name + "(" + this.arguments.prettyPrint("") + ")" + temp;
    }
}