package com.finch.app;

public class MemberExpr extends Expr {
    public String name;

    MemberExpr(String name, Expr expr) {
        this.id = "Expr";
        this.exprID = "Member";
        this.name = name;
    }
}