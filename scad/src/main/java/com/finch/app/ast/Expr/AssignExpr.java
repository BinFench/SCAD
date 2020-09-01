package com.finch.app;

public class AssignExpr extends Expr {
    public String name;
    public String operator;
    public Expr value;
    public Expr var;
    public Boolean member;
    public Boolean hasValue;

    AssignExpr(String name, String op, Expr value) {
        this.id = "Expr";
        this.exprID = "Assign";
        this.name = name;
        this.value = value;
        this.operator = op;
        this.member = false;
        this.hasValue = true;
    }

    AssignExpr(String name, String op) {
        this.id = "Expr";
        this.exprID = "Assign";
        this.name = name;
        this.operator = op;
        this.member = false;
        this.hasValue = false;
    }

    AssignExpr(Expr member, String op, Expr value) {
        this.id = "Expr";
        this.exprID = "Assign";
        this.name = member.name;
        this.value = value;
        this.var = member;
        this.operator = op;
        this.member = true;
        this.hasValue = true;
    }

    AssignExpr(Expr member, String op) {
        this.id = "Expr";
        this.exprID = "Assign";
        this.name = member.name;
        this.var = member;
        this.operator = op;
        this.member = true;
        this.hasValue = false;
    }

    public String prettyPrint(String temp, Boolean label) {
        System.out.println("ASSIGN[");
        String toPrint;
        if (this.member) {
            toPrint = (label ? "[ASSIGN]" : "") + this.var.prettyPrint("", label) + " " + this.operator + " " + (this.hasValue ? this.value.prettyPrint(temp, label) : "");
        } else {
            toPrint = (label ? "[ASSIGN]" : "") + this.name + " " + this.operator + " " + (this.hasValue ? this.value.prettyPrint(temp, label) : "");
        }

        System.out.println("]ASSIGN");
        return toPrint;
    }
}