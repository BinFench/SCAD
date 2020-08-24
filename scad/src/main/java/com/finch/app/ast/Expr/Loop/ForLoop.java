package com.finch.app;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class ForLoop extends LoopExpr {
    public String varName;
    public Var var;
    public Condition condition;
    public Expr expr;
    
    ForLoop(String varName, Condition condition, Expr expr) {
        this.id = "Expr";
        this.exprID = "Loop";
        this.loopID = "For";
        this.varName = varName;
        this.condition = condition;
        this.expr = expr;
        this.scopes = new ArrayList<Scope>();
    }

    ForLoop(Var var, Condition condition, Expr expr) {
        this.id = "Expr";
        this.exprID = "Loop";
        this.loopID = "For";
        this.varName = var.name;
        this.var = var;
        this.condition = condition;
        this.expr = expr;
        this.scopes = new ArrayList<Scope>();
    }
}