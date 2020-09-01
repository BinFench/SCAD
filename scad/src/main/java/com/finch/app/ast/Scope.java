package com.finch.app;

public class Scope extends CommonNode {
    String id;
    String origString;
   
    public Scope addString(String og) {
        this.origString = og;
        return this;
    }
    
    public String prettyPrint(String temp, Boolean label) {
        return temp;
    }

    public String format(String temp) {
        String[] lines = temp.split("\n");
        String toFormat = "";

        for (int i = 0; i < lines.length; i++) {
            if (i != 0) {
                toFormat += "\t" + lines[i];
            } else {
                toFormat += lines[i];
            }

            if (i != lines.length - 1) {
                toFormat += "\n";
            }
        }
        return toFormat;
    }
}