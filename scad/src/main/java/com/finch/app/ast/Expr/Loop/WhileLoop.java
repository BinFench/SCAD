package com.finch.app;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class WhileLoop extends LoopExpr {
    public ConditionExpr condition;
    
    WhileLoop(ConditionExpr condition) {
        this.id = "Expr";
        this.exprID = "Loop";
        this.loopID = "While";
        this.condition = condition;
        this.scopes = new ArrayList<Scope>();
    }

    public String prettyPrint() {
        String toPrint = "while(" + this.condition.prettyPrint("") + ") {\n";
        for (int i = 0; i < this.scopes.size(); i++) {
            toPrint += "\t" + format(getScope(i).prettyPrint(";")) + "\n";
        }
        return toPrint + "}";
    }
}