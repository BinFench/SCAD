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
    }

    ConditionalExpr addCondition(ConditionExpr condition) {
        this.condition = condition;

        return this;
    }

    ConditionalExpr addConditional(ConditionalExpr conditional) {
        this.conditionals.add(conditional);

        return this;
    }

    ConditionalExpr addScope(Scope scope) {
        this.scopes.add(scope);

        return this;
    }

    public Scope getScope(int i) {
        return this.scopes.get(i);
    }

    ConditionalExpr setElse(Boolean hasElse) {
        this.hasElse = hasElse;

        return this;
    }

    ConditionalExpr addElseScope(Scope scope) {
        this.elseScopes.add(scope);

        return this;
    }
}