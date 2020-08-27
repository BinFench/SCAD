package com.finch.app;

public class AssignExpr extends Expr {
    public String name;
    public Term value;

    AssignExpr(String name, Term value) {
        this.id = "Expr";
        this.exprID = "Assign";
        this.name = name;
        this.value = value;
    }
}