package com.finch.app;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class LoopExpr extends Expr {
    public String loopID;
    public List<Scope> scopes;

    LoopExpr addScope(Scope scope) {
        this.scopes.add(scope);
        return this;
    }
    
    public Scope getScope(int i) {
        return this.scopes.get(i);
    }
}