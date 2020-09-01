package com.finch.app;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class ConditionalExpr extends Expr {
    public ConditionExpr condition;
    public List<Scope> scopes;
    public List<ConditionalExpr> conditionals;
    public Boolean hasElse;
    public List<Scope> elseScopes;

    ConditionalExpr() {
        this.id = "Expr";
        this.exprID = "Conditional";
        this.scopes = new ArrayList<Scope>();
        this.conditionals = new ArrayList<ConditionalExpr>();
        this.elseScopes = new ArrayList<Scope>();
        this.hasElse = false;
    }

    ConditionalExpr addCondition(ConditionExpr condition) {
        this.condition = condition;

        return this;
    }

    public ConditionalExpr addConditional(ConditionalExpr conditional) {
        this.conditionals.add(conditional);

        return this;
    }

    public ConditionalExpr addScope(Scope scope) {
        this.scopes.add(scope);

        return this;
    }

    public Scope getScope(int i) {
        return this.scopes.get(i);
    }

    public Scope getElseScope(int i) {
        return this.elseScopes.get(i);
    }

    public ConditionalExpr getConditional(int i) {
        return this.conditionals.get(i);
    }

    public ConditionalExpr setElse(Boolean hasElse) {
        this.hasElse = hasElse;

        return this;
    }

    public ConditionalExpr addElseScope(Scope scope) {
        this.elseScopes.add(scope);
        this.hasElse = true;

        return this;
    }

    public String prettyPrint(String temp) {
        String toPrint = "if(" + this.condition.prettyPrint("") + ") {\n";
        for (int i = 0; i < this.scopes.size(); i++) {
            toPrint += "\t" + format(getScope(i).prettyPrint(";")) + "\n";
        }
        toPrint += "}";
        for (int i = 0; i < this.conditionals.size(); i++) {
            toPrint += " else " + getConditional(i).prettyPrint("");
        }
        if (this.hasElse) {
            toPrint += " else {\n";
            for (int i = 0; i < this.elseScopes.size(); i++) {
                toPrint += "\t" + format(getElseScope(i).prettyPrint(";")) + "\n";
            }
            toPrint += "}";
        }
        return toPrint;
    }
}