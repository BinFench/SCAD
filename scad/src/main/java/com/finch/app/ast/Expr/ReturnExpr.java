package com.finch.app;

public class ReturnExpr extends Expr {
    public Expr term;

    ReturnExpr(Expr term) {
        this.id = "Expr";
        this.exprID = "Return";
        this.term = term;
    }

    public String prettyPrint(String temp, Boolean label) {
        System.out.println("RETURN[");
        String toPrint = (label ? "[RETURN]" : "") + "return " + this.term.prettyPrint("", label) + ";";
        System.out.println("]RETURN");
        return toPrint;
    }
}