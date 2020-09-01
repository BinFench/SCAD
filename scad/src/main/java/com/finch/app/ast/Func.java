package com.finch.app;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Func extends Scope {
    public String type;
    public String name;
    public List<Scope> scopes;
    public List<Var> vars;
    public List<Expr> exprs;
    public Argument arguments;

    Func(String type, String name, Argument arguments) {
        this.type = type;
        this.name = name;
        this.scopes = new ArrayList<Scope>();
        this.id = "Func";
        this.arguments = arguments;
    }

    public Func addScope(Scope scope) {
        if (scope.id != "Func" && scope.id != "Class") {
            this.scopes.add(scope);
        } else {
            throw new IllegalParsableException("Cannot declare function or class within function");
        }
        return this;
    }

    public Scope getScope(int i) {
        return this.scopes.get(i);
    }

    public Var getVar(int i) {
        return this.vars.get(i);
    }

    public Expr getExpr(int i) {
        return this.exprs.get(i);
    }

    @Override
    public String prettyPrint(String temp) {
        this.vars = new ArrayList<Var>();
        this.exprs = new ArrayList<Expr>();

        String type = "Start";
        String exprType = "Start";
        Boolean done = false;

        for (int i = 0; i < this.scopes.size(); i++) {
            if (getScope(i) instanceof Var) {
                this.vars.add((Var)getScope(i));
            } else if (getScope(i) instanceof Expr) {
                this.exprs.add((Expr)getScope(i));
            } else {
                throw new IllegalParsableException("Illegal scope: Function scope can only contain Vars, and Statement Expressions.");
            }
        }

        String toPrint = this.type + " " + this.name + "(" + this.arguments.prettyPrint("") + ") {\n";
        
        for (int i = 0; i < this.vars.size(); i++) {
            type = "Var";
            toPrint += "\t" + getVar(i).prettyPrint(";");
            if (i < this.vars.size() - 1) {
                toPrint += "\n";
            }
        }

        for (int i = 0; i < this.exprs.size(); i++) {
            if (type != "Start" && type != "Expr") {
                toPrint += "\n\n";
            }
            type = "Expr";
            
            switch(getExpr(i).exprID) {
                case "Assign":
                    if (exprType != "Start" && exprType != "Assign") {
                        toPrint += "\n\n";
                    } else if (exprType == "Assign") {
                        toPrint += "\n";
                    }
                    exprType = "Assign";
                    break;

                case "Conditional":
                    if (exprType != "Start") {
                        toPrint += "\n\n";
                    }
                    exprType = "Conditional";
                    break;

                case "FuncCall":
                    if (exprType != "Start" && exprType != "FuncCall") {
                        toPrint += "\n\n";
                    } else if (exprType == "FuncCall") {
                        toPrint += "\n";
                    }
                    exprType = "FuncCall";
                    break;

                case "Member":
                    if (exprType != "Start" && exprType != "Member") {
                        toPrint += "\n\n";
                    } else if (exprType == "Member") {
                        toPrint += "\n";
                    }
                    exprType = "Member";
                    break;

                case "Op":
                    if (exprType != "Start" && exprType != "Op") {
                        toPrint += "\n\n";
                    } else if (exprType == "Op") {
                        toPrint += "\n";
                    }
                    exprType = "Op";
                    break;

                case "Loop":
                    if (exprType != "Start") {
                        toPrint += "\n\n";
                    }
                    exprType = "Loop";
                    break;

                case "Return":
                    if (exprType != "Start") {
                        toPrint += "\n\n";
                    }
                    exprType = "Return";
                    done = true;
                    break;

                default:
                    throw new IllegalParsableException("Illegal scope: Function scope can only contain Vars, and Statement Expressions.");
            }
            toPrint += "\t" + format(getExpr(i).prettyPrint(";"));

            if (done) {
                break;
            }
        }

        return toPrint + "\n}";
    }
}