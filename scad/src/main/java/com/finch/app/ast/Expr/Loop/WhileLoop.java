package com.finch.app;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class WhileLoop extends LoopExpr {
    public Condition condition;
    
    WhileLoop(Condition condition) {
        this.id = "Expr";
        this.exprID = "Loop";
        this.loopID = "While";
        this.condition = condition;
        this.scopes = new ArrayList<Scope>();
    }
}