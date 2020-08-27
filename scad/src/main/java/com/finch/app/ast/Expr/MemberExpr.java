package com.finch.app;

public class MemberExpr extends Expr {
    public String name;

    MemberExpr(String name) {
        this.id = "Expr";
        this.exprID = "Member";
        this.name = name;
    }
}