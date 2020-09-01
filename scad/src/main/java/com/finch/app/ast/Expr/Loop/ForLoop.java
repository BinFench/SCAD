package com.finch.app;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class ForLoop extends LoopExpr {
    public String varName;
    public Var var;
    public ConditionExpr condition;
    public Expr expr;
    public Boolean hasVar;
    
    ForLoop(Expr expr, ConditionExpr condition) {
        this.id = "Expr";
        this.exprID = "Loop";
        this.loopID = "For";
        this.condition = condition;
        this.expr = expr;
        this.scopes = new ArrayList<Scope>();
        this.hasVar = false;
    }

    public ForLoop addVar(Object obj) {
        if (obj instanceof String) {
            this.varName = (String)obj;
        } else {
            this.var = (Var)obj;
            this.varName = this.var.name;
            this.hasVar = true;
        }
        return this;
    }

    public String prettyPrint() {
        String toPrint = "for(";
        if (this.hasVar) {
            toPrint += this.var.prettyPrint("") + "; ";
        } else {
            toPrint += this.varName + "; ";
        }
        toPrint += this.condition.prettyPrint("") + "; " + this.expr.prettyPrint("") + ") {\n";
        for (int i = 0; i < this.scopes.size(); i++) {
            toPrint += "\t" + format(getScope(i).prettyPrint(";")) + "\n";
        }
        return toPrint + "}";
    }
}