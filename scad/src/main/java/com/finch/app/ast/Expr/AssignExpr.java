package com.finch.app;

public class AssignExpr extends Expr {
    public String name;
    public String operator;
    public Expr value;
    public Expr var;
    public Boolean member;

    AssignExpr(String name, String op, Expr value) {
        this.id = "Expr";
        this.exprID = "Assign";
        this.name = name;
        this.value = value;
        this.operator = op;
        this.member = false;
    }

    AssignExpr(Expr member, String op, Expr value) {
        this.id = "Expr";
        this.exprID = "Assign";
        this.name = member.name;
        this.value = value;
        this.var = member;
        this.operator = op;
        this.member = true;
    }
}