package com.finch.app;

public class ReturnExpr extends Expr {
    public Expr term;

    ReturnExpr(Expr term) {
        this.id = "Expr";
        this.exprID = "Return";
        this.term = term;
    }
}