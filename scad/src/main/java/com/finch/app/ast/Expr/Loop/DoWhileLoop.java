package com.finch.app;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class DoWhileLoop extends LoopExpr {
    public Condition condition;
    public List<Scope> scopes;
    
    DoWhileLoop(Condition condition) {
        this.id = "Expr";
        this.exprID = "Loop";
        this.loopID = "DoWhile";
        this.condition = condition;
        this.scopes = new ArrayList<Scope>();
    }
}