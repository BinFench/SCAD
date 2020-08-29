package com.finch.app;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class DoWhileLoop extends LoopExpr {
    public ConditionExpr condition;
    public List<Scope> scopes;
    
    DoWhileLoop() {
        this.id = "Expr";
        this.exprID = "Loop";
        this.loopID = "DoWhile";
        this.scopes = new ArrayList<Scope>();
    }

    public DoWhileLoop addCondition(ConditionExpr condition) {
        this.condition = condition;
        return this;
    }
}