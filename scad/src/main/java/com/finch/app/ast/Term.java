package com.finch.app;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Term {
    public List<Term> subTerms;
    public List<Value> values;
    public List<Integer> alignment;
    public List<String> operations;
    //Alignment: 0 for subterm, 1 for value
    //align an operation to a term/value pair

    Term() {
        this.subTerms = new ArrayList<Term>();
        this.values = new ArrayList<Value>();
        this.alignment = new ArrayList<Integer>();
        this.operations = new ArrayList<String>();
    }

    public Term addTerm(Term term) {
        this.subTerms.add(term);
        this.alignment.add(0);
        return this;
    }

    public Term addValue(Value value) {
        this.values.add(value);
        this.alignment.add(1);
        return this;
    }

    public Term addOperation(String operation) {
        this.operations.add(operation);
        return this;
    }
}