package com.fenrir.filesorter.model.rule;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenContainerTest {

    @Test
    public void iteratorShouldReturnIterator() {
        TokenContainer container = new TokenContainer();
        Iterator<Token> iterator = container.iterator();
        assertNotNull(iterator);
    }

    @Test
    public void addShouldAddRuleElement() {
        TokenContainer container = new TokenContainer();
        Token element = new Token("abc", null);
        container.add(element);
        Iterator<Token> iterator = container.iterator();
        assertEquals(element, iterator.next());
        assertFalse(iterator.hasNext());
    }
}