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
    public void addShouldAddToken() {
        TokenContainer container = new TokenContainer();
        Token token = new Token("abc", null);
        container.add(token);
        Iterator<Token> iterator = container.iterator();
        assertEquals(token, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void equalShouldReturnTrueIfObjectsAreTheSame() {
        TokenContainer container1 = new TokenContainer();
        container1.add(new Token("abc", null));
        container1.add(new Token("bcd", null));
        TokenContainer container2 = new TokenContainer();
        container2.add(new Token("abc", null));
        container2.add(new Token("bcd", null));
        assertEquals(container1, container2);
    }

    @Test
    public void equalShouldReturnFalseForDistinctToken() {
        TokenContainer container1 = new TokenContainer();
        container1.add(new Token("abc", null));
        container1.add(new Token("bcd", null));
        TokenContainer container2 = new TokenContainer();
        container2.add(new Token("abc", null));
        container2.add(new Token("dist", null));
        assertNotEquals(container1, container2);
    }

    @Test
    public void equalShouldReturnFalseForSameTokensInDifferentOrder() {
        TokenContainer container1 = new TokenContainer();
        container1.add(new Token("abc", null));
        container1.add(new Token("bcd", null));
        TokenContainer container2 = new TokenContainer();
        container2.add(new Token("bcd", null));
        container2.add(new Token("abc", null));
        assertNotEquals(container1, container2);
    }

    @Test
    public void equalShouldReturnFalseIfOneObjectIsEqualNull() {
        TokenContainer container1 = new TokenContainer();
        container1.add(new Token("abc", null));
        container1.add(new Token("bcd", null));
        assertNotEquals(container1, null);
    }

    @Test
    public void equalShouldReturnFalseForObjectsOfADifferentType() {
        TokenContainer container1 = new TokenContainer();
        container1.add(new Token("abc", null));
        container1.add(new Token("bcd", null));
        assertNotEquals(container1, new Object());
    }
}