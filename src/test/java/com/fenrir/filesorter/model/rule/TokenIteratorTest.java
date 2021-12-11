package com.fenrir.filesorter.model.rule;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TokenIteratorTest {

    @Test
    public void hasNextShouldReturnFalseForEmptyList() {
        Iterator<Token> iterator = new TokenIterator(new ArrayList<>());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void nextShouldReturnNullForEmptyList() {
        Iterator<Token> iterator = new TokenIterator(new ArrayList<>());
        assertNull(iterator.next());
    }

    @Test
    public void nextShouldReturnNextElement() {
        List<Token> elements = List.of(
                new Token("A",  null),
                new Token("B",  null),
                new Token("C",  new ArrayList<>())
        );
        Iterator<Token> iterator = new TokenIterator(elements);
        assertEquals(elements.get(0), iterator.next());
        assertEquals(elements.get(1), iterator.next());
        assertEquals(elements.get(2), iterator.next());
        assertNull(iterator.next());
    }

    @Test
    public void resetShouldResetIterator() {
        List<Token> elements = List.of(
                new Token("A", null),
                new Token("B", null),
                new Token("C",  new ArrayList<>())
        );
        Iterator<Token> iterator = new TokenIterator(elements);
        iterator.next();
        assertEquals(elements.get(1), iterator.next());
        iterator.reset();
        assertEquals(elements.get(0), iterator.next());
    }

}