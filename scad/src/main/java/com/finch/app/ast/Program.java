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

    public String prettyPrint(Boolean label) {
        System.out.println("PROGRAM[");
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
            toPrint += getVar(i).prettyPrint(";", label);
            if (i < this.vars.size() - 1) {
                toPrint += "\n";
            }
        }

        for (int i = 0; i < this.classes.size(); i++) {
            if (type == "Var") {
                toPrint += "\n\n";
            }
            type = "Class";
            toPrint += getClass(i).prettyPrint("", label);
            if (i < this.classes.size() - 1) {
                toPrint += "\n\n";
            }
        }

        for (int i = 0; i < this.funcs.size(); i++) {
            if (type != "Start" && type != "Func") {
                toPrint += "\n\n";
            }
            type = "Func";
            toPrint += getFunc(i).prettyPrint("", label);
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
            toPrint += getExpr(i).prettyPrint(";", label);

            if (done) {
                break;
            }
        }

        System.out.println("]PROGRAM");

        return toPrint;
    }

    public Value interpret() {
        List<Var> interpretedVars = new ArrayList<Var>(); //These vars are global scope
        List<Func> candidateFuncs;
        Value value;

        for (int i = 0; i < this.scopes.size(); i++) {
            if (getScope(i) instanceof Var) {
                interpretedVars.add((Var)getScope(i));
            } else if (getScope(i) instanceof Expr) {
                //TODO: interpret expressions
                Expr expr = (Expr)getScope(i);
                switch(expr.exprID) {
                    case "Assign":
                        AssignExpr assign = (AssignExpr)expr;
                        String name = assign.name;
                        String operator = assign.operator;
                        Var interpretedVar = null;
                        int match = -1;
                        String matchType = "";
                        Func interpretedFunc = null;
                        Class interpretedClass = null;
                        TermExpr newterm = new TermExpr();
                        
                        for (int j = 0; j < interpretedVars.size(); j++) {
                            interpretedVar = interpretedVars.get(j);
                            if (name == interpretedVar.name) {
                                match = j;
                                matchType = "Var";
                                break;
                            }
                        }

                        if (match < 0 && !assign.member) {
                            throw new IllegalParsableException("Error: unknown variable " + name);
                        }

                        candidateFuncs = new ArrayList<Func>();
                        for (int j = 0; j < this.funcs.size(); j++) {
                            interpretedFunc = (Func)this.funcs.get(j);
                            if (name == interpretedFunc.name) {
                                match = j;
                                matchType = "Func";
                                candidateFuncs.add(interpretedFunc);
                            }
                        }

                        for (int j = 0; j < this.classes.size(); j++) {
                            interpretedClass = this.classes.get(j);
                            if (name == interpretedClass.name) {
                                if (matchType == "Var") {
                                    throw new IllegalParsableException("Error: cannot name variable after type " + name);
                                } else if (matchType == "Func") {
                                    throw new IllegalParsableException("Error: Constructor function must be within class " + name);
                                }
                                match = j;
                                matchType = "Class";
                                break;
                            }
                        }

                        switch(matchType) {
                            case "Var":

                                switch (interpretedVar.type) {
                                    case "int":
                                        if (assign.member) {
                                            throw new IllegalParsableException("Error: Members from this type are non assignable: int");
                                        }

                                        if (assign.hasValue) {
                                            value = assign.value.interpret();
                                            if (value.valID == "Int" || value.valID == "Float") {
                                                switch(operator) {
                                                    case "=":
                                                        interpretedVar.assign(assign.value);
                                                    break

                                                    case "+=":
                                                        if (interpretedVar.assigned) {
                                                            newterm.addValue(interpretedVar.value.interpret());
                                                            newterm.addOperation("+");
                                                            newterm.addValue(value);
                                                            interpretedVar.assign(newterm);
                                                        } else {
                                                            throw new IllegalParsableException("Error: Illegal assignment to unassigned variable " + name);
                                                        }
                                                    break

                                                    case "-=":
                                                        if (interpretedVar.assigned) {
                                                            newterm.addValue(interpretedVar.value.interpret());
                                                            newterm.addOperation("-");
                                                            newterm.addValue(value);
                                                            interpretedVar.assign(newterm);
                                                        } else {
                                                            throw new IllegalParsableException("Error: Illegal assignment to unassigned variable " + name);
                                                        }
                                                    break

                                                    case "*=":
                                                        if (interpretedVar.assigned) {
                                                            newterm.addValue(interpretedVar.value.interpret());
                                                            newterm.addOperation("*");
                                                            newterm.addValue(value);
                                                            interpretedVar.assign(newterm);
                                                        } else {
                                                            throw new IllegalParsableException("Error: Illegal assignment to unassigned variable " + name);
                                                        }
                                                    break

                                                    case "/=":
                                                        if (interpretedVar.assigned) {
                                                            newterm.addValue(interpretedVar.value.interpret());
                                                            newterm.addOperation("/");
                                                            newterm.addValue(value);
                                                            interpretedVar.assign(newterm);
                                                        } else {
                                                            throw new IllegalParsableException("Error: Illegal assignment to unassigned variable " + name);
                                                        }
                                                    break

                                                    case "^=":
                                                        if (interpretedVar.assigned) {
                                                            newterm.addValue(interpretedVar.value.interpret());
                                                            newterm.addOperation("^");
                                                            newterm.addValue(value);
                                                            interpretedVar.assign(newterm);
                                                        } else {
                                                            throw new IllegalParsableException("Error: Illegal assignment to unassigned variable " + name);
                                                        }
                                                    break

                                                    case "&=":
                                                        if (interpretedVar.assigned) {
                                                            newterm.addValue(interpretedVar.value.interpret());
                                                            newterm.addOperation("&");
                                                            newterm.addValue(value);
                                                            interpretedVar.assign(newterm);
                                                        } else {
                                                            throw new IllegalParsableException("Error: Illegal assignment to unassigned variable " + name);
                                                        }
                                                    break

                                                    case "|=":
                                                        if (interpretedVar.assigned) {
                                                            newterm.addValue(interpretedVar.value.interpret());
                                                            newterm.addOperation("|");
                                                            newterm.addValue(value);
                                                            interpretedVar.assign(newterm);
                                                        } else {
                                                            throw new IllegalParsableException("Error: Illegal assignment to unassigned variable " + name);
                                                        }
                                                    break

                                                    case "^^=":
                                                        if (interpretedVar.assigned) {
                                                            newterm.addValue(interpretedVar.value.interpret());
                                                            newterm.addOperation("^^");
                                                            newterm.addValue(value);
                                                            interpretedVar.assign(newterm);
                                                        } else {
                                                            throw new IllegalParsableException("Error: Illegal assignment to unassigned variable " + name);
                                                        }
                                                    break

                                                    case ">>=":
                                                        if (interpretedVar.assigned) {
                                                            newterm.addValue(interpretedVar.value.interpret());
                                                            newterm.addOperation(">>");
                                                            newterm.addValue(value);
                                                            interpretedVar.assign(newterm);
                                                        } else {
                                                            throw new IllegalParsableException("Error: Illegal assignment to unassigned variable " + name);
                                                        }
                                                    break

                                                    case "<<=":
                                                        if (interpretedVar.assigned) {
                                                            newterm.addValue(interpretedVar.value.interpret());
                                                            newterm.addOperation("<<");
                                                            newterm.addValue(value);
                                                            interpretedVar.assign(newterm);
                                                        } else {
                                                            throw new IllegalParsableException("Error: Illegal assignment to unassigned variable " + name);
                                                        }
                                                    break

                                                    default:
                                                        throw new IllegalParsableException("Error: Invalid operator " + operator);
                                                    break
                                                }
                                            } else {
                                                throw new IllegalParsableException("Error: cannot assign " + value.valID + " to int.");
                                            }
                                        } else {
                                            if (interpretedVar.assigned) {
                                                if (operator.contains("!")) {
                                                    throw new IllegalParsableException("Error: Invalid operator " + operator);
                                                } else if (operator.contains("++")) {
                                                    newterm.addValue(interpretedVar.value.interpret());
                                                    newterm.addOperation("+");
                                                    newterm.addValue(new IntValue(1));
                                                } else if (operator.contains("--")) {
                                                    newterm.addValue(interpretedVar.value.interpret());
                                                    newterm.addOperation("-");
                                                    newterm.addValue(new IntValue(1));
                                                }
                                            } else {
                                                throw new IllegalParsableException("Error: Uninitialized variable " + name);
                                            }
                                        }
                                    break;

                                    case "float":
                                        if (assign.member) {
                                            throw new IllegalParsableException("Error: Members from this type are non assignable: float");
                                        }

                                        if (assign.hasValue) {
                                            value = assign.value.interpret();
                                            if (value.valID == "Int" || value.valID == "Float") {
                                                switch(operator) {
                                                    case "=":
                                                        interpretedVar.assign(assign.value);
                                                    break

                                                    case "+=":
                                                        if (interpretedVar.assigned) {
                                                            newterm.addValue(interpretedVar.value.interpret());
                                                            newterm.addOperation("+");
                                                            newterm.addValue(value);
                                                            interpretedVar.assign(newterm);
                                                        } else {
                                                            throw new IllegalParsableException("Error: Illegal assignment to unassigned variable " + name);
                                                        }
                                                    break

                                                    case "-=":
                                                        if (interpretedVar.assigned) {
                                                            newterm.addValue(interpretedVar.value.interpret());
                                                            newterm.addOperation("-");
                                                            newterm.addValue(value);
                                                            interpretedVar.assign(newterm);
                                                        } else {
                                                            throw new IllegalParsableException("Error: Illegal assignment to unassigned variable " + name);
                                                        }
                                                    break

                                                    case "*=":
                                                        if (interpretedVar.assigned) {
                                                            newterm.addValue(interpretedVar.value.interpret());
                                                            newterm.addOperation("*");
                                                            newterm.addValue(value);
                                                            interpretedVar.assign(newterm);
                                                        } else {
                                                            throw new IllegalParsableException("Error: Illegal assignment to unassigned variable " + name);
                                                        }
                                                    break

                                                    case "/=":
                                                        if (interpretedVar.assigned) {
                                                            newterm.addValue(interpretedVar.value.interpret());
                                                            newterm.addOperation("/");
                                                            newterm.addValue(value);
                                                            interpretedVar.assign(newterm);
                                                        } else {
                                                            throw new IllegalParsableException("Error: Illegal assignment to unassigned variable " + name);
                                                        }
                                                    break

                                                    case "^=":
                                                        if (interpretedVar.assigned) {
                                                            newterm.addValue(interpretedVar.value.interpret());
                                                            newterm.addOperation("^");
                                                            newterm.addValue(value);
                                                            interpretedVar.assign(newterm);
                                                        } else {
                                                            throw new IllegalParsableException("Error: Illegal assignment to unassigned variable " + name);
                                                        }
                                                    break

                                                    case "&=":
                                                        if (interpretedVar.assigned) {
                                                            newterm.addValue(interpretedVar.value.interpret());
                                                            newterm.addOperation("&");
                                                            newterm.addValue(value);
                                                            interpretedVar.assign(newterm);
                                                        } else {
                                                            throw new IllegalParsableException("Error: Illegal assignment to unassigned variable " + name);
                                                        }
                                                    break

                                                    case "|=":
                                                        if (interpretedVar.assigned) {
                                                            newterm.addValue(interpretedVar.value.interpret());
                                                            newterm.addOperation("|");
                                                            newterm.addValue(value);
                                                            interpretedVar.assign(newterm);
                                                        } else {
                                                            throw new IllegalParsableException("Error: Illegal assignment to unassigned variable " + name);
                                                        }
                                                    break

                                                    case "^^=":
                                                        if (interpretedVar.assigned) {
                                                            newterm.addValue(interpretedVar.value.interpret());
                                                            newterm.addOperation("^^");
                                                            newterm.addValue(value);
                                                            interpretedVar.assign(newterm);
                                                        } else {
                                                            throw new IllegalParsableException("Error: Illegal assignment to unassigned variable " + name);
                                                        }
                                                    break

                                                    case ">>=":
                                                        if (interpretedVar.assigned) {
                                                            newterm.addValue(interpretedVar.value.interpret());
                                                            newterm.addOperation(">>");
                                                            newterm.addValue(value);
                                                            interpretedVar.assign(newterm);
                                                        } else {
                                                            throw new IllegalParsableException("Error: Illegal assignment to unassigned variable " + name);
                                                        }
                                                    break

                                                    case "<<=":
                                                        if (interpretedVar.assigned) {
                                                            newterm.addValue(interpretedVar.value.interpret());
                                                            newterm.addOperation("<<");
                                                            newterm.addValue(value);
                                                            interpretedVar.assign(newterm);
                                                        } else {
                                                            throw new IllegalParsableException("Error: Illegal assignment to unassigned variable " + name);
                                                        }
                                                    break

                                                    default:
                                                        throw new IllegalParsableException("Error: Invalid operator " + operator);
                                                    break
                                                }
                                            } else {
                                                throw new IllegalParsableException("Error: cannot assign " + value.valID + " to float.");
                                            }
                                        } else {
                                            if (interpretedVar.assigned) {
                                                if (operator.contains("!")) {
                                                    throw new IllegalParsableException("Error: Invalid operator " + operator);
                                                } else if (operator.contains("++")) {
                                                    newterm.addValue(interpretedVar.value.interpret());
                                                    newterm.addOperation("+");
                                                    newterm.addValue(new IntValue(1));
                                                } else if (operator.contains("--")) {
                                                    newterm.addValue(interpretedVar.value.interpret());
                                                    newterm.addOperation("-");
                                                    newterm.addValue(new IntValue(1));
                                                }
                                            } else {
                                                throw new IllegalParsableException("Error: Uninitialized variable " + name);
                                            }
                                        }
                                    break;

                                    case "bool":
                                        if (assign.member) {
                                            throw new IllegalParsableException("Error: Members from this type are non assignable: bool");
                                        }

                                        if (assign.hasValue) {
                                            value = assign.value.interpret();
                                            if (value.valID == "Bool") {
                                                switch(operator) {
                                                    case "=":
                                                        interpretedVar.assign(assign.value);
                                                    break

                                                    case "^^=":
                                                        if (interpretedVar.assigned) {
                                                            newterm.addValue(interpretedVar.value.interpret());
                                                            newterm.addOperation("^^");
                                                            newterm.addValue(value);
                                                            interpretedVar.assign(newterm);
                                                        } else {
                                                            throw new IllegalParsableException("Error: Illegal assignment to unassigned variable " + name);
                                                        }
                                                    break

                                                    case "&=":
                                                        if (interpretedVar.assigned) {
                                                            newterm.addValue(interpretedVar.value.interpret());
                                                            newterm.addOperation("&&");
                                                            newterm.addValue(value);
                                                            interpretedVar.assign(newterm);
                                                        } else {
                                                            throw new IllegalParsableException("Error: Illegal assignment to unassigned variable " + name);
                                                        }
                                                    break

                                                    case "|=":
                                                        if (interpretedVar.assigned) {
                                                            newterm.addValue(interpretedVar.value.interpret());
                                                            newterm.addOperation("||");
                                                            newterm.addValue(value);
                                                            interpretedVar.assign(newterm);
                                                        } else {
                                                            throw new IllegalParsableException("Error: Illegal assignment to unassigned variable " + name);
                                                        }
                                                    break

                                                    default:
                                                        throw new IllegalParsableException("Error: Invalid operator " + operator);
                                                    break
                                                }
                                            } else {
                                                throw new IllegalParsableException("Error: cannot assign " + value.valID + " to bool.");
                                            }
                                        } else {
                                            if (interpretedVar.assigned) {
                                                if (operator.contains("!")) {
                                                    newterm.addValue(new BoolValue(!((BoolValue)interpretedVar.value.interpret()).value));
                                                } else {
                                                    throw new IllegalParsableException("Error: Invalid operator " + operator);
                                                }
                                            } else {
                                                throw new IllegalParsableException("Error: Uninitialized variable " + name);
                                            }
                                        }
                                    break;
                                }
                            break;
                        }


                    break;

                    case "Conditional":

                    break;

                    case "FuncCall":

                    break;

                    case "Member":

                    break;

                    case "Op":

                    break;

                    case "Return":

                    break;

                    default:

                    break;
                }
                break;
            }
        }

        return new VoidValue();
    }
}