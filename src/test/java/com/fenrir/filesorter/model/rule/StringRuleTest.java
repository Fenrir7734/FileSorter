package com.fenrir.filesorter.model.rule;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringRuleTest {

    @Test
    public void resolveRuleWithoutFlags() {
        Rule rule = new StringRule("abcd");
        Iterator<RuleElement> iterator = rule.getRuleElementsIterator();

        RuleElement element = iterator.next();
        assertEquals("abcd", element.element());
        assertFalse(element.isToken());

        element = iterator.next();
        assertNull(element);
    }

    @Test
    void resolveRuleContainingOneElement() {
        Rule rule = new StringRule("-");
        Iterator<RuleElement> iterator = rule.getRuleElementsIterator();

        RuleElement element = iterator.next();
        assertEquals("-", element.element());
        assertFalse(element.isToken());

        element = iterator.next();
        assertNull(element);
    }

    @Test
    void resolveRuleContainingOnlyFlags() {
        Rule rule = new StringRule("%(YYYY)%(MM)%(/)%(DD)");
        Iterator<RuleElement> iterator = rule.getRuleElementsIterator();

        RuleElement element = iterator.next();
        assertEquals("YYYY", element.element());
        assertTrue(element.isToken());

        element = iterator.next();
        assertEquals("MM", element.element());
        assertTrue(element.isToken());

        element = iterator.next();
        assertEquals("/", element.element());
        assertTrue(element.isToken());

        element = iterator.next();
        assertEquals("DD", element.element());
        assertTrue(element.isToken());

        element = iterator.next();
        assertNull(element);
    }

    @Test
    public void resolveRuleWithFlagAtTheBeginning() {
        Rule rule = new StringRule("%(YYYY)-%(MM)%(/)%(DD)-");
        Iterator<RuleElement> iterator = rule.getRuleElementsIterator();

        RuleElement element = iterator.next();
        assertEquals("YYYY", element.element());
        assertTrue(element.isToken());

        element = iterator.next();
        assertEquals("-", element.element());
        assertFalse(element.isToken());

        element = iterator.next();
        assertEquals("MM", element.element());
        assertTrue(element.isToken());

        element = iterator.next();
        assertEquals("/", element.element());
        assertTrue(element.isToken());

        element = iterator.next();
        assertEquals("DD", element.element());
        assertTrue(element.isToken());

        element = iterator.next();
        assertEquals("-", element.element());
        assertFalse(element.isToken());

        element = iterator.next();
        assertNull(element);
    }

    @Test
    public void resolveRuleWithFlagAtTheEnd() {
        Rule rule = new StringRule("-%(YYYY)-%(MM)%(/)%(DD)");
        Iterator<RuleElement> iterator = rule.getRuleElementsIterator();

        RuleElement element = iterator.next();
        assertEquals("-", element.element());
        assertFalse(element.isToken());

        element = iterator.next();
        assertEquals("YYYY", element.element());
        assertTrue(element.isToken());

        element = iterator.next();
        assertEquals("-", element.element());
        assertFalse(element.isToken());

        element = iterator.next();
        assertEquals("MM", element.element());
        assertTrue(element.isToken());

        element = iterator.next();
        assertEquals("/", element.element());
        assertTrue(element.isToken());

        element = iterator.next();
        assertEquals("DD", element.element());
        assertTrue(element.isToken());

        element = iterator.next();
        assertNull(element);
    }

    @Test
    public void resolveRuleWithFlagsAtBothEnds() {
        Rule rule = new StringRule("%(YYYY)-%(MM)%(/)%(DD)");
        Iterator<RuleElement> iterator = rule.getRuleElementsIterator();

        RuleElement element = iterator.next();
        assertEquals("YYYY", element.element());
        assertTrue(element.isToken());

        element = iterator.next();
        assertEquals("-", element.element());
        assertFalse(element.isToken());

        element = iterator.next();
        assertEquals("MM", element.element());
        assertTrue(element.isToken());

        element = iterator.next();
        assertEquals("/", element.element());
        assertTrue(element.isToken());

        element = iterator.next();
        assertEquals("DD", element.element());
        assertTrue(element.isToken());

        element = iterator.next();
        assertNull(element);
    }
}
