package com.finch.app;

public class AssignExpr extends Expr {
    public String name;
    public Expr value;

    AssignExpr(String name, Expr value) {
        this.id = "Expr";
        this.exprID = "Assign";
        this.name = name;
        this.value = value;
    }
}