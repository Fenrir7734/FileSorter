package com.fenrir.filesorter.model.rule;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RuleElementIteratorTest {

    @Test
    public void hasNextShouldReturnFalseForEmptyList() {
        Iterator<RuleElement> iterator = new RuleElementIterator(new ArrayList<>());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void nextShouldReturnNullForEmptyList() {
        Iterator<RuleElement> iterator = new RuleElementIterator(new ArrayList<>());
        assertNull(iterator.next());
    }

    @Test
    public void nextShouldReturnNextElement() {
        List<RuleElement> elements = List.of(
                new RuleElement("A", false, null),
                new RuleElement("B", true, null),
                new RuleElement("C", true, new String[5])
        );
        Iterator<RuleElement> iterator = new RuleElementIterator(elements);
        assertEquals(elements.get(0), iterator.next());
        assertEquals(elements.get(1), iterator.next());
        assertEquals(elements.get(2), iterator.next());
        assertNull(iterator.next());
    }

    @Test
    public void resetShouldResetIterator() {
        List<RuleElement> elements = List.of(
                new RuleElement("A", false, null),
                new RuleElement("B", true, null),
                new RuleElement("C", true, new String[5])
        );
        Iterator<RuleElement> iterator = new RuleElementIterator(elements);
        iterator.next();
        assertEquals(elements.get(1), iterator.next());
        iterator.reset();
        assertEquals(elements.get(0), iterator.next());
    }

}