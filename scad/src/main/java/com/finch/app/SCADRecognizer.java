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

@BuildParseTree
public class SCADRecognizer extends BaseParser<Object>
{
    public static void main( String[] args )
    {
        final String input = "int a = 5;";
        parse(input);
    }

    public static Boolean parse(String input) {
        SCADRecognizer parser = Parboiled.createParser(SCADRecognizer.class);
        ParsingResult<?> result = new ReportingParseRunner(parser.Program()).run(input);
        Boolean ret = result.parseErrors.isEmpty();
        if (ret) {
            String parseTreePrintOut = ParseTreeUtils.printNodeTree(result);
            System.out.println(parseTreePrintOut);
        } else {
            for (int i = 0; i < result.parseErrors.size(); i++) {
                System.out.println(ErrorUtils.printParseError(result.parseErrors.get(i)));
            }
        }
        return ret;
    }

    public Rule Program() {
        return Sequence(
            W0(),
            OneOrMore(
                Sequence(
                    Scope(),
                    W0()
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
            Type(), W1(), Name(), Optional(W0(), "=", W0(), Term()), W0(), ";"
        );
    }

    public Rule Function() {
        return Sequence(
            Type(), W1(), Name(), W0(), "(", W0(), Arguments(), ")", W0(), "{",
                W0(),
                OneOrMore(
                    Sequence(
                        Scope(),
                        W0()
                    )
                ),
            "}"
        );
    }

    public Rule Class() {
        return Sequence(
            Name(), W0(), "{",
                W0(),
                OneOrMore(
                    Sequence(
                        Scope(),
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
            Optional(
                Sequence(
                    W0(),
                    "(",
                    W0(),
                    Arguments(),
                    W0(),
                    ")"
                )
            )
        );
    }

    public Rule Expression() {
        return FirstOf(
                Sequence(
                    Name(), W0(), "(", W0(), Arguments(), ")", W0(), ";"
                ),
                Conditional(),
                Loop(),
                Sequence(
                    Assignment(), W0(), ";"
                ),
                Sequence(
                    "return", W0(), Term(), W0(), ";"
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
        return Optional(
            FirstOf(
                Sequence(
                    Type(), W1(), Name(), W0(),
                    ZeroOrMore(
                        Sequence(
                            ",", W0(), Type(), W1(), Name(), W0()
                        )
                    )
                ),
                Sequence(
                    Term(), W0(),
                    ZeroOrMore(
                        Sequence(
                            ",", W0(), Term(), W0()
                        )
                    )
                )
            )
        );
    }

    public Rule Value() {
        return FirstOf(
            Sequence(
                Optional(
                    Sequence(
                        AnyOf("+-"), 
                        W0()
                    )
                ),
                Integer(), Optional(FirstOf("x", "y", "z", "%"))
            ),
            Sequence(
                Optional(
                    Sequence(
                        AnyOf("+-"), 
                        W0()
                    )
                ),
                Float(), Optional(FirstOf("x", "y", "z", "%"))
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
                Optional(
                    Sequence(
                        W0(), "(", W0(), Arguments(), W0(), ")"
                    )
                )
            ),
            Member(),
            Operator()
        );
    }

    public Rule Conditional() {
        return Sequence(
            "if", W0(), "(", W0(), Condition(), 
            ZeroOrMore(
                Sequence(
                    W0(),
                    Math(),
                    W0(),
                    Condition()
                )
            ), W0(), ")", W0(), "{",
                W0(),
                OneOrMore(
                    Sequence(
                        Scope(),
                        W0()
                    )
                ),
            "}",
            Optional(
                FirstOf(
                    Sequence(W0(), "else", W0(), Conditional()),
                    Sequence(
                        W0(), "else", W0(), "{",
                            W0(),
                            OneOrMore(
                                Sequence(
                                    Scope(),
                                    W0()
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
                    W0(),
                    Term()
                )
            )
        );
    }

    public Rule Loop() {
        return FirstOf(
            Sequence(
                "do", W0(), "{",
                    W0(),
                    OneOrMore(
                        Scope(),
                        W0()
                    ),
                "}", W0(), "while", W0(), "(", W0(), Condition(), 
                ZeroOrMore(
                    Sequence(
                        W0(),
                        Math(),
                        W0(),
                        Condition()
                    )
                ),
                W0(), ")", W0(), ";"
            ),
            Sequence(
                "while", W0(), "(", W0(), Condition(), 
                ZeroOrMore(
                    Sequence(
                        W0(),
                        Math(),
                        W0(),
                        Condition()
                    )
                ),
                W0(), ")", W0(), "{",
                    W0(),
                    OneOrMore(
                        Scope(),
                        W0()
                    ),
                "}"
            ),
            Sequence(
                "for", W0(), "(",
                    W0(), FirstOf(Variable(), Sequence(Name(), W0(), ";")), 
                    W0(), Condition(), 
                    ZeroOrMore(
                        Sequence(
                            W0(),
                            Math(),
                            W0(),
                            Condition()
                        )
                    ),
                    W0(), ";",
                    W0(), Assignment(), W0(),
                ")", W0(), "{",
                    W0(),
                    OneOrMore(
                        Scope(),
                        W0()
                    ),
                "}"
            )
        );
    }

    public Rule Assignment() {
        return FirstOf(
            Sequence(Name(), W0(), Assign(), W0(), Term()),
            Sequence(Name(), W0(), Inc(), W0()),
            Sequence(Inc(), W0(), Name(), W0())
        );
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
        return FirstOf(
            Formula(),
            Value(),
            Sequence("(", W0(), Term(), W0(), ")")
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
                            AnyOf("`~!@#$%^&*()-_=+|]}[{;:/?.>,<")
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
                            AnyOf("`~!@#$%^&*()-_=+|]}[{;:/?.>,<")
                        )
                    ),
                    "'"
                )
            ),
            Optional(
                Sequence(
                    W0(),
                    "%",
                    W0(),
                    Arguments()
                )
            )
        );
    }

    public Rule Member() {
        return Sequence(
            Name(),
            Optional(
                Sequence(
                    W0(), "(", W0(), Arguments(), W0(), ")"
                )
            ),
            OneOrMore(
                W0(),
                ".",
                W0(),
                Name(),
                Optional(
                    Sequence(
                        W0(), "(", W0(), Arguments(), W0(), ")"
                    )
                )
            )
        );
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
            W0(), Math(), W0(), Term()
        );
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
