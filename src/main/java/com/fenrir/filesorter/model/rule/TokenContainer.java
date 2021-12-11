package com.fenrir.filesorter.model.rule;

import java.util.ArrayList;
import java.util.List;

public class TokenContainer {
    private final List<Token> ruleElements = new ArrayList<>();

    protected void add(Token element) {
        this.ruleElements.add(element);
    }

    public Iterator<Token> iterator() {
        return new TokenIterator(ruleElements);
    }
}
