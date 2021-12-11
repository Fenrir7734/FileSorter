package com.fenrir.filesorter.model.rule;

import java.util.List;

public class RuleElementIterator implements Iterator<RuleElement> {
    private final List<RuleElement> expressionElements;
    private int currentPosition = 0;

    public RuleElementIterator(List<RuleElement> expressionElements) {
        this.expressionElements = expressionElements;
    }

    @Override
    public boolean hasNext() {
        return currentPosition < expressionElements.size();
    }

    @Override
    public RuleElement next() {
        return hasNext() ? expressionElements.get(currentPosition++) : null;
    }

    @Override
    public void reset() {
        currentPosition = 0;
    }
}
