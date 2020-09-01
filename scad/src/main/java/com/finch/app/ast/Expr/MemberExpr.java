package com.finch.app;

public class MemberExpr extends Expr {
    public String name;
    public Expr member;
    public Argument arguments;
    public Boolean hasArgs;

    MemberExpr(String name) {
        this.id = "Expr";
        this.exprID = "Member";
        this.name = name;
        this.hasArgs = false;
    }

    public MemberExpr addMember(Expr member) {
        this.member = member;
        return this;
    }

    public MemberExpr addArg(Argument arg) {
        this.arguments = arg;
        this.hasArgs = true;
        return this;
    }

    public String prettyPrint(String temp, Boolean label) {
        System.out.println("MEMBER[");
        String toPrint = (label ? "[MEMBER]" : "") + this.name;
        if (this.hasArgs) {
            toPrint += "(" + this.arguments.prettyPrint("", label) + ")";
        }
        toPrint += "." + this.member.prettyPrint(temp, label);
        System.out.println("]MEMBER");
        return toPrint;
    }
}