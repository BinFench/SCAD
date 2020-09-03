package com.finch.app;

import org.parboiled.trees.ImmutableGraphNode;

public class CommonNode extends ImmutableGraphNode {
    public Value interpret() {
        return new VoidValue();
    }
}