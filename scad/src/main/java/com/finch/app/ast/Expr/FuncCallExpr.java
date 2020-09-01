package com.finch.app;

public class FuncCallExpr extends Expr {
    public String name;
    public Argument arguments;

    FuncCallExpr(String name, Argument arguments) {
        this.id = "Expr";
        this.exprID = "FuncCall";
        this.name = name;
        this.arguments = arguments;
    }

    public String prettyPrint(String temp, Boolean label) {
        System.out.println("FUNCCALL[");
        String toPrint = (label ? "[FUNCCALL]" : "") + this.name + "(" + this.arguments.prettyPrint("", label) + ")" + temp;
        System.out.println("]FUNCCALL");
        return toPrint;
    }
}