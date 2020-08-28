package com.finch.app;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class TermExpr extends Expr {
    public List<Expr> subTerms;
    public List<Value> values;
    public List<Integer> alignment;
    public List<String> operations;
    //Alignment: 0 for subterm, 1 for value
    //align an operation to a term/value pair

    TermExpr() {
        this.id = "Expr";
        this.exprID = "Term";
        this.subTerms = new ArrayList<Expr>();
        this.values = new ArrayList<Value>();
        this.alignment = new ArrayList<Integer>();
        this.operations = new ArrayList<String>();
    }

    public TermExpr addTerm(TermExpr term) {
        this.subTerms.add(term);
        this.alignment.add(0);
        return this;
    }

    public TermExpr addValue(Value value) {
        this.values.add(value);
        this.alignment.add(1);
        return this;
    }

    public TermExpr addOperation(String operation) {
        this.operations.add(operation);
        return this;
    }
}