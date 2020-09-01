package com.finch.app;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Program extends CommonNode {
    public List<Scope> scopes;
    public List<Func> funcs;
    public List<Class> classes;
    public List<Var> vars;
    public List<Expr> exprs;

    Program() {
        this.scopes = new ArrayList<Scope>();
    }

    public Program addScope(Scope scope) {
        this.scopes.add(scope);
        return this;
    }

    public Scope getScope(int i) {
        return this.scopes.get(i);
    }

    public Func getFunc(int i) {
        return this.funcs.get(i);
    }

    public Class getClass(int i) {
        return this.classes.get(i);
    }

    public Var getVar(int i) {
        return this.vars.get(i);
    }

    public Expr getExpr(int i) {
        return this.exprs.get(i);
    }

    public String prettyPrint() {
        this.funcs = new ArrayList<Func>();
        this.classes = new ArrayList<Class>();
        this.vars = new ArrayList<Var>();
        this.exprs = new ArrayList<Expr>();

        String toPrint = "";
        String type = "start";
        String exprType = "start";
        Boolean done = false;

        for (int i = 0; i < this.scopes.size(); i++) {
            if (getScope(i) instanceof Func) {
                this.funcs.add((Func)getScope(i));
            } else if (getScope(i) instanceof Class) {
                this.classes.add((Class)getScope(i));
            } else if (getScope(i) instanceof Var) {
                this.vars.add((Var)getScope(i));
            } else if (getScope(i) instanceof Expr) {
                this.exprs.add((Expr)getScope(i));
            } else {
                throw new IllegalParsableException("Illegal scope: global scope can only contain Functions, Classes, Vars, and Statement Expressions.");
            }
        }

        for (int i = 0; i < this.vars.size(); i++) {
            type = "Var";
            toPrint += getVar(i).prettyPrint(";");
            if (i < this.vars.size() - 1) {
                toPrint += "\n";
            }
        }

        for (int i = 0; i < this.classes.size(); i++) {
            if (type == "Var") {
                toPrint += "\n\n";
            }
            type = "Class";
            toPrint += getClass(i).prettyPrint("");
            if (i < this.classes.size() - 1) {
                toPrint += "\n\n";
            }
        }

        for (int i = 0; i < this.funcs.size(); i++) {
            if (type != "Start" && type != "Func") {
                toPrint += "\n\n";
            }
            type = "Func";
            toPrint += getFunc(i).prettyPrint("");
            if (i < this.funcs.size() - 1) {
                toPrint += "\n\n";
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
                    throw new IllegalParsableException("Illegal scope: global scope can only contain Functions, Classes, Vars, and Statement Expressions.");
            }
            toPrint += getExpr(i).prettyPrint(";");

            if (done) {
                break;
            }
        }

        return toPrint;
    }
}