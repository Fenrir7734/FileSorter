package com.fenrir.filesorter.model.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TokenContainer {
    private final List<Token> ruleElements = new ArrayList<>();

    protected void add(Token element) {
        this.ruleElements.add(element);
    }

    public Iterator<Token> iterator() {
        return new TokenIterator(ruleElements);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenContainer that = (TokenContainer) o;
        return Objects.equals(ruleElements, that.ruleElements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleElements);
    }
}
