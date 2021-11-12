package com.fenrir.filesorter.model.rule;

import java.util.ArrayList;
import java.util.List;

public class RuleElementContainer {
    private final List<RuleElement> ruleElements = new ArrayList<>();

    protected void add(RuleElement element) {
        this.ruleElements.add(element);
    }

    public Iterator<RuleElement> iterator() {
        return new RuleElementIterator(ruleElements);
    }
}
