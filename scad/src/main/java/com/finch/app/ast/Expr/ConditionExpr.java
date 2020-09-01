package com.finch.app;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class ConditionExpr extends Expr {
    public List<Expr> terms;
    public List<String> operators;

    ConditionExpr(Expr term, String op) {
        this.id = "Expr";
        this.exprID = "Condition";
        this.terms = new ArrayList<Expr>();
        this.operators = new ArrayList<String>();
        terms.add(term);
        operators.add(op);
    }

    public ConditionExpr addCondition(Expr term, String op) {
        terms.add(term);
        operators.add(op);
        return this;
    }

    public Expr getTerm(int i) {
        return this.terms.get(i);
    }

    public String getOp(int i) {
        return this.operators.get(i);
    }

    public String prettyPrint(String temp) {
        String toPrint = getTerm(0).prettyPrint("");

        for (int i = 1; i < this.operators.size(); i++) {
            toPrint += " " + getOp(i) + " " + getTerm(i).prettyPrint("");
        }
        return toPrint + temp;
    }
}