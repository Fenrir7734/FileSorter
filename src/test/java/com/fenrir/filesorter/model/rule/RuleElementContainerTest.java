package com.fenrir.filesorter.model.rule;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RuleElementContainerTest {

    @Test
    public void iteratorShouldReturnIterator() {
        RuleElementContainer container = new RuleElementContainer();
        Iterator<RuleElement> iterator = container.iterator();
        assertNotNull(iterator);
    }

    @Test
    public void addShouldAddRuleElement() {
        RuleElementContainer container = new RuleElementContainer();
        RuleElement element = new RuleElement("abc", false, null);
        container.add(element);
        Iterator<RuleElement> iterator = container.iterator();
        assertEquals(element, iterator.next());
        assertFalse(iterator.hasNext());
    }
}