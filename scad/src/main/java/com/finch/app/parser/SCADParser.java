package com.finch.app;

import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.parserunners.TracingParseRunner;
import org.parboiled.support.ParsingResult;
import org.parboiled.support.ToStringFormatter;
import org.parboiled.trees.GraphNode;
import org.parboiled.common.FileUtils;
import org.parboiled.common.ImmutableList;
import org.parboiled.support.ParseTreeUtils;
import org.parboiled.errors.ErrorUtils;
import org.parboiled.*;

public class SCADParser extends BaseParser<Object>
{
    public static void main( String[] args )
    {
        final String input = "int fib(int n) {\n" +
            "    if (n <= 1) {\n" +
            "        return n;\n" +
            "    } else {\n" +
            "        return fib(n-1) + fib(n-2);\n" +
            "    }\n" +
            "}";
        parse(input);
    }

    public static Boolean parse(String input) {
        SCADParser parser = Parboiled.createParser(SCADParser.class);
        ParsingResult<?> result = new ReportingParseRunner(parser.Program()).run(input);
        Boolean ret = result.parseErrors.isEmpty();
        if (ret) {
            Program prog = (Program)result.resultValue;
            System.out.println(prog.prettyPrint());
        } else {
            for (int i = 0; i < result.parseErrors.size(); i++) {
                System.out.println(ErrorUtils.printParseError(result.parseErrors.get(i)));
            }
        }
        return ret;
    }

    public Rule Program() {
        return Sequence(
            push(new Program()),
            W0(),
            OneOrMore(
                Sequence(
                    Scope(),
                    W0(),
                    swap(),
                    push(((Program)pop()).addScope((Scope)pop()))
                )
            ),
            EOI
        );
    }

    public Rule Scope() {
        return FirstOf(
            Function(),
            Class(),
            Variable(),
            Operator(),
            Expression()
        );
    }

    public Rule Variable() {
        return Sequence(
            Type(),
            push(match()),
            W1(),
            Name(),
            push(match()),
            swap(),
            push(new Var((String)pop(), (String)pop())),
            Optional(
                W0(),
                "=",
                W0(),
                Term(),
                swap(),
                push(((Var)pop()).assign((Expr)pop()))
            ),
            W0(),
            ";"
        );
    }

    public Rule Function() {
        return Sequence(
            Type(),
            push(match()),
            W1(),
            Name(),
            push(match()),
            W0(),
            "(",
            W0(),
            Arguments(),
            push(newFunction((Argument)pop(), (String)pop(), (String)pop())),
            ")",
            W0(),
            "{",
                W0(),
                OneOrMore(
                    Sequence(
                        Scope(),
                        swap(),
                        push(((Func)pop()).addScope((Scope)pop())),
                        W0()
                    )
                ),
            "}"
        );
    }

    public Func newFunction(Argument arg, String name, String type) {
        return new Func(type, name, arg);
    }

    public Rule Class() {
        return Sequence(
            Name(),
            push(new Class(match())),
            W0(), "{",
                W0(),
                OneOrMore(
                    Sequence(
                        Scope(),
                        swap(),
                        push(((Class)pop()).addScope((Scope)pop())),
                        W0()
                    )
                ),
            "}"
        );
    }

    public Rule Operator() {
        return Sequence(
            FirstOf(
                "Point",
                "Line",
                "Segment",
                "Arc",
                "Curve",
                "Face",
                "Plane",
                "Extrude",
                "Offset",
                "Radial",
                "Rotate",
                "Translate",
                "Scale",
                "Union",
                "Intersection",
                "Complement",
                "Decompose",
                "Bind",
                "Copy",
                "At",
                "Select"
            ),
            push(match()),
            W0(),
            "(",
            W0(),
            Arguments(),
            swap(),
            W0(),
            ")",
            push(new OpExpr((String)pop(), (Argument)pop()))
        );
    }

    public Rule Expression() {
        return FirstOf(
            Sequence(
                Name(),
                push(match()),
                W0(), "(", W0(), Arguments(), ")", W0(), ";",
                swap(),
                push(new FuncCallExpr((String)pop(), (Argument)pop()))
            ),
            Conditional(),
            Loop(),
            Sequence(
                Assignment(), W0(), ";"
            ),
            Sequence(
                "return", W0(), Term(), W0(), ";",
                push(new ReturnExpr((Expr)pop()))
            ),
            Sequence(Member(), W0(), ";")
        );
    }

    public Rule Type() {
        return FirstOf(
            "int",
            "float",
            "bool",
            Sequence(
                "Array<", W0(), Type(), W0(), ">"
            ),
            Name(),
            "void",
            "shape"
        );
    }

    public Rule Name() {
        return Sequence(
            TestNot(
                Sequence(
                    Reserved(),
                    FirstOf(
                        W1(),
                        "(",
                        ")",
                        ";",
                        "{",
                        "}",
                        ","
                    )
                )
            ),
            OneOrMore(
                Sequence(
                    FirstOf(
                        CharRange('a', 'z'), 
                        CharRange('A', 'Z')
                    ), 
                    ZeroOrMore(
                        FirstOf(
                            CharRange('0','9'), 
                            Ch('_')
                        )
                    )
                )
            )
        );
    }

    public Rule Arguments() {
        return Sequence(
            push(new Argument()),
            Optional(
                FirstOf(
                    Sequence(
                        Type(),
                        push(match()),
                        W1(), Name(),
                        push(match()),
                        push(addArg((String)pop(), (String)pop(), (Argument)pop())),
                        W0(),
                        ZeroOrMore(
                            Sequence(
                                ",", W0(), Type(),
                                push(match()),
                                W1(), Name(),
                                push(match()),
                                push(addArg((String)pop(), (String)pop(), (Argument)pop())),
                                W0()
                            )
                        )
                    ),
                    Sequence(
                        Term(),
                        swap(),
                        push(((Argument)pop()).addVal((Expr)pop())),
                        W0(),
                        ZeroOrMore(
                            Sequence(
                                ",", W0(), Term(),
                                swap(),
                                push(((Argument)pop()).addVal((Expr)pop())),
                                W0()
                            )
                        )
                    )
                )
            )
        );
    }

    public Argument addArg(String name, String type, Argument arg) {
        return arg.addVar(new Var(type, name));
    }

    public Rule Value() {
        return Sequence(
            Sequence(
                Optional(Inc()),
                FirstOf(
                    Sequence(
                        Optional(
                            Sequence(
                                AnyOf("+-"), 
                                W0()
                            )
                        ),
                        Integer(), 
                        push(new IntValue(Integer.parseInt(match()))),
                        Optional(FirstOf("x", "y", "z", "%"))
                    ),
                    Sequence(
                        Optional(
                            Sequence(
                                AnyOf("+-"), 
                                W0()
                            )
                        ),
                        Float(),
                        push(new FloatValue(Double.parseDouble(match()))),
                        Optional(FirstOf("x", "y", "z", "%"))
                    ),
                    String(),
                    Sequence(
                        Optional(
                            Sequence(
                                AnyOf("+-"), 
                                W0()
                            )
                        ),
                        Name(),
                        push(new VarValue(match())),
                        push(match()),
                        Optional(
                            Sequence(
                                W0(), "(", W0(), Arguments(), W0(), ")",
                                swap(),
                                push(new FuncValue((String)pop(), (Argument)pop())),
                                swap()
                            )
                        ),
                        new Action() {
                            public boolean run(Context context) {
                                pop();
                                return true;
                            }
                        }
                    ),
                    Member(),
                    Operator(),
                    Array()
                ),
                Optional(Inc())
            ),
            push(((Scope)pop()).addString(match()))
        );
    }

    public Rule Conditional() {
        return Sequence(
            "if", W0(), "(", W0(), Condition(), 
            ZeroOrMore(
                Sequence(
                    W0(),
                    Math(),
                    push(match()),
                    swap(),
                    W0(),
                    Condition(),
                    swap(),
                    push(((ConditionExpr)pop()).addCondition((Expr)pop(), (String)pop()))
                )
            ),
            push(new ConditionalExpr()),
            push(((ConditionalExpr)pop()).addCondition((ConditionExpr)pop())),
            W0(), ")", W0(), "{",
                W0(),
                OneOrMore(
                    Sequence(
                        Scope(),
                        W0(),
                        swap(),
                        push(((ConditionalExpr)pop()).addScope((Scope)pop()))
                    )
                ),
            "}",
            Optional(
                FirstOf(
                    Sequence(
                        W0(), "else", W0(), Conditional(),
                        swap(),
                        push(((ConditionalExpr)pop()).addConditional((ConditionalExpr)pop()))
                    ),
                    Sequence(
                        W0(), "else", W0(), "{",
                            W0(),
                            OneOrMore(
                                Sequence(
                                    Scope(),
                                    W0(),
                                    swap(),
                                    push(((ConditionalExpr)pop()).addElseScope((Scope)pop()))
                                )
                            ),
                        "}"
                    )
                )
            )
        );
    }

    public Rule Condition() {
        return Sequence(
            Term(),
            Optional(
                Sequence(
                    W0(),
                    Comparator(),
                    push(((TermExpr)pop()).addOperation(match())),
                    W0(),
                    Term(),
                    swap(),
                    push(((TermExpr)pop()).addTerm((TermExpr)pop()))
                )
            ),
            push(new ConditionExpr((TermExpr)pop(), ""))
        );
    }

    public Rule Loop() {
        return FirstOf(
            Sequence(
                "do", W0(), "{",
                    W0(),
                    push(new DoWhileLoop()),
                    OneOrMore(
                        Scope(),
                        swap(),
                        push(((ForLoop)pop()).addScope((Scope)pop())),
                        W0()
                    ),
                "}", W0(), "while", W0(), "(", W0(), Condition(), 
                ZeroOrMore(
                    Sequence(
                        W0(),
                        Math(),
                        push(match()),
                        swap(),
                        W0(),
                        Condition(),
                        swap(),
                        push(((ConditionExpr)pop()).addCondition((ConditionExpr)pop(), (String)pop()))
                    )
                ),
                W0(), ")", W0(), ";",
                swap(),
                push(((DoWhileLoop)pop()).addCondition((ConditionExpr)pop()))
            ),
            Sequence(
                "while", W0(), "(", W0(), Condition(), 
                ZeroOrMore(
                    Sequence(
                        W0(),
                        Math(),
                        push(match()),
                        swap(),
                        W0(),
                        Condition(),
                        swap(),
                        push(((ConditionExpr)pop()).addCondition((ConditionExpr)pop(), (String)pop()))
                    )
                ),
                W0(), ")", W0(), "{",
                    W0(),
                    push(new WhileLoop((ConditionExpr) pop())),
                    OneOrMore(
                        Scope(),
                        swap(),
                        push(((WhileLoop)pop()).addScope((Scope)pop())),
                        W0()
                    ),
                "}"
            ),
            Sequence(
                "for", W0(), "(", W0(),
                FirstOf(
                    Variable(),
                    Sequence(
                        Name(),
                        push(match()),
                        W0(),
                        ";")
                ), 
                W0(), Condition(), 
                ZeroOrMore(
                    Sequence(
                        W0(),
                        Math(),
                        push(match()),
                        swap(),
                        W0(),
                        Condition(),
                        swap(),
                        push(((ConditionExpr)pop()).addCondition((ConditionExpr)pop(), (String)pop()))
                    )
                ),
                W0(), ";",
                W0(), Assignment(), W0(),
                ")", W0(), "{",
                   W0(),
                   push(new ForLoop((AssignExpr)pop(), (ConditionExpr)pop())),
                   push(((ForLoop)pop()).addVar(pop())),
                   OneOrMore(
                   Scope(),
                   swap(),
                   push(((ForLoop)pop()).addScope((Scope)pop())),
                   W0()
                   ),
                "}"
            )
        );
    }

    public Rule Assignment() {
        return FirstOf(
            Sequence(
                Name(),
                push(match()),
                W0(),
                Assign(),
                push(match()),
                W0(),
                Term(),
                push(getAssign((Expr)pop(), (String)pop(), (String)pop()))
            ),
            Sequence(
                Name(),
                push(match()),
                W0(),
                Inc(),
                push(match()),
                swap(),
                W0(),
                push(new AssignExpr((String)pop(), "post" + (String)pop(), new TermExpr()))
            ),
            Sequence(
                Inc(),
                push(match()),
                W0(),
                Name(),
                push(new AssignExpr(match(), "pre" + (String)pop(), new TermExpr())),
                W0()
            ),
            Sequence(
                Member(),
                W0(),
                Assign(),
                push(match()),
                W0(),
                Term(),
                push(getMember((Expr)pop(), (String)pop(), (Expr)pop()))            ),
            Sequence(
                Member(),
                W0(),
                Inc(),
                push(match()),
                swap(),
                W0(),
                push(new AssignExpr((Expr)pop(), "post" + (String)pop(), new TermExpr()))
            ),
            Sequence(
                Inc(),
                push(match()),
                W0(),
                Member(),
                push(new AssignExpr((Expr)pop(), "pre" + (String)pop(), new TermExpr())),
                W0()
            )
        );
    }

    public AssignExpr getAssign(Expr value, String op, String name) {
        return new AssignExpr(name, op, value);
    }

    public AssignExpr getMember(Expr value, String op, Expr member) {
        return new AssignExpr(member, op, value);
    }

    public Rule Assign() {
        return FirstOf(
            "=",
            "+=",
            "-=",
            "*=",
            "/=",
            "^=",
            "&=",
            "|=",
            "^^=",
            ">>=",
            "<<="
        );
    }

    public Rule Term() {
        return Sequence(
            FirstOf(
                Formula(),
                Value(),
                Sequence("(", W0(), Term(), W0(), ")")
            ),
            push(addItem(pop()))
        );
    }

    public Rule Comparator() {
        return FirstOf(
            "==",
            "!=",
            ">=",
            "<=",
            "<",
            ">"
        );
    }

    public Rule Inc() {
        return FirstOf(
            "++",
            "--",
            "!"
        );
    }

    public Rule Math() {
        return FirstOf(
            "+",
            "-",
            "*",
            "/",
            "&&",
            "||",
            "^",
            "^^",
            "<<",
            ">>",
            "+-"
        );
    }

    public Rule Integer() {
        return OneOrMore(
            CharRange('0','9')
        );
    }

    public Rule Float() {
        return Sequence(
            Integer(),
            Optional(
                Sequence(
                    ".",
                    Integer()
                )
            )
        );
    }

    public Rule String() {
        return Sequence(
            FirstOf(
                Sequence(
                    "\"",
                    ZeroOrMore(
                        FirstOf(
                            Escape(),
                            Mods(),
                            W1(),
                            CharRange('a', 'z'),
                            CharRange('A', 'Z'),
                            CharRange('0', '9'),
                            AnyOf("`~!@#$%^&*()-_=+|]}[{;:/?.>,<'")
                        )
                    ),
                    "\""
                ),
                Sequence(
                    "'",
                    ZeroOrMore(
                        FirstOf(
                            Escape(),
                            Mods(),
                            W1(),
                            CharRange('a', 'z'),
                            CharRange('A', 'Z'),
                            CharRange('0', '9'),
                            AnyOf("`~!@#$%^&*()-_=+|]}[{;:/?.>,<\"")
                        )
                    ),
                    "'"
                )
            ),
            push(new StringValue(match())),
            Optional(
                Sequence(
                    W0(),
                    "%",
                    W0(),
                    Arguments(),
                    swap(),
                    push(((StringValue)pop()).addArg((Argument)pop()))
                )
            )
        );
    }

    public Rule Member() {
        return Sequence(
            Name(),
            push(new MemberExpr(match())),
            Optional(
                Sequence(
                    W0(), "(", W0(), Arguments(), W0(), ")",
                    swap(),
                    push(((MemberExpr)pop()).addArg((Argument)pop()))
                )
            ),
            W0(),
            ".",
            W0(),
            MemberChild(),
            swap(),
            push(((MemberExpr)pop()).addMember((MemberExpr)pop()))
        );
    }

    public Rule MemberChild() {
        return Sequence(
            Name(),
            push(new MemberExpr(match())),
            Optional(
                Sequence(
                    W0(), "(", W0(), Arguments(), W0(), ")",
                    swap(),
                    push(((MemberExpr)pop()).addArg((Argument)pop()))
                )
            ),
            Optional(
                Sequence(
                    W0(),
                    ".",
                    W0(),
                    MemberChild(),
                    swap(),
                    push(((MemberExpr)pop()).addMember((MemberExpr)pop()))
                )
            )
        );
    }

    public Rule Array() {
        return Sequence(
            "[",
            W0(),
            Value(),
            push(getArray((Value)pop())),
            W0(),
            ZeroOrMore(
                Sequence(
                    ",",
                    W0(),
                    Value(),
                    swap(),
                    push(((ArrayValue)pop()).addValue((Value)pop())),
                    W0()
                )
            ),
            "]"
        );
    }

    public ArrayValue getArray(Value val) {
        String type;
        if (val instanceof ArrayValue) {
            type = "Array<" + ((ArrayValue)val).type + ">";
        } else {
            type = val.valID;
        }
        return new ArrayValue(type);
    }

    public Rule Escape() {
        return Sequence(
            "\\",
            FirstOf(
                "t",
                "b",
                "n",
                "r",
                "f",
                "'",
                '"',
                "\\"
            )
        );
    }

    public Rule Mods() {
        return Sequence(
            "%",
            FirstOf(
                "d",
                "i",
                "o",
                "u",
                "x",
                "X",
                "e",
                "E",
                "f",
                "F",
                "g",
                "G",
                "c",
                "r",
                "s",
                "%"
            )
        );
    }

    public Rule Formula() {
        return Sequence(
            FirstOf(
                Value(),
                Sequence(
                    "(",
                    W0(),
                    Term(),
                    W0(),
                    ")"
                )
            ),
            push(addItem(pop())),
            W0(), 
            FirstOf(Math(), Comparator()),
            push(((TermExpr)pop()).addOperation(match())),
            W0(), Term(),
            swap(),
            push(((TermExpr)pop()).addTerm((TermExpr)pop()))
        );
    }

    public TermExpr addItem(Object obj) {
        if (obj instanceof TermExpr) {
            return (TermExpr)obj;
        } else {
            TermExpr toRet = new TermExpr();
            return toRet.addValue((Value)obj);
        }
    }

    public Rule Reserved() {
        return FirstOf(
            "do",
            "while",
            "for",
            "if",
            "else",
            "return",
            "int",
            "float",
            "bool",
            "void",
            "shape",
            Operator()
        );
    }

    public Rule W() {
		return AnyOf(" \t\f\n\r");
    }
    
    public Rule W0() {
        return ZeroOrMore(W());
    }

    public Rule W1() {
        return OneOrMore(W());
    }
}
