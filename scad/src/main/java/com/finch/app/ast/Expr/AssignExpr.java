package com.finch.app;

public class AssignExpr extends Expr {
    public String name;
    public Value value;

    AssignExpr(String name, Value value) {
        this.id = "Expr";
        this.exprID = "Assign";
        this.name = name;
        this.value = value;
    }
}