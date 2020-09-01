package com.finch.app;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class DoWhileLoop extends LoopExpr {
    public ConditionExpr condition;
    
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

    public String prettyPrint(String temp, Boolean label) {
        System.out.println("DOWHILE[");
        String toPrint = (label ? "[DOWHILE]" : "") + "do {\n";
        for (int i = 0; i < this.scopes.size(); i++) {
            toPrint += "\t" + format(getScope(i).prettyPrint(";", label)) + "\n";
        }
        toPrint += "} while(" + condition.prettyPrint("", label) + ");";
        System.out.println("]DOWHILE");
        return toPrint;
    }
}