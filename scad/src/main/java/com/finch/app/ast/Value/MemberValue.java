package com.finch.app;

public class MemberValue extends Value {
    public Expr expr;
    MemberValue(Expr expr) {
        this.id = "Value";
        this.valID = "Member";
        this.expr = expr;
    }

    public String prettyPrint(String temp, Boolean label) {
        System.out.println("MEMBERVALUE[");
        String toPrint = (label ? "[MEMBERVALUE]" : "") + this.expr.prettyPrint(temp, label);
        System.out.println("]MEMBERVALUE");
        return toPrint;
    }
}