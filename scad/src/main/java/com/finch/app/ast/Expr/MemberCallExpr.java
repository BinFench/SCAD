package com.finch.app;

public class MemberCallExpr extends Expr {
    public String name;
    public Expr expr;

    MemberCallExpr(String name, Expr expr) {
        this.id = "Expr";
        this.exprID = "MemberCall";
        this.name = name;
        this.expr = expr;
    }
}