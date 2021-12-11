package com.fenrir.filesorter.model.rule;

import java.util.List;

public class TokenIterator implements Iterator<Token> {
    private final List<Token> expressionElements;
    private int currentPosition = 0;

    public TokenIterator(List<Token> expressionElements) {
        this.expressionElements = expressionElements;
    }

    @Override
    public boolean hasNext() {
        return currentPosition < expressionElements.size();
    }

    @Override
    public Token next() {
        return hasNext() ? expressionElements.get(currentPosition++) : null;
    }

    @Override
    public void reset() {
        currentPosition = 0;
    }
}
