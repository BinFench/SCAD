package com.finch.app;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class ForLoop extends LoopExpr {
    public String varName;
    public Var var;
    public ConditionExpr condition;
    public Expr expr;
    
    ForLoop(Expr expr, ConditionExpr condition) {
        this.id = "Expr";
        this.exprID = "Loop";
        this.loopID = "For";
        this.condition = condition;
        this.expr = expr;
        this.scopes = new ArrayList<Scope>();
    }

    public ForLoop addVar(Object obj) {
        if (obj instanceof String) {
            this.varName = (String)obj;
        } else {
            this.var = (Var)obj;
            this.varName = this.var.name;
        }
        return this;
    }
}