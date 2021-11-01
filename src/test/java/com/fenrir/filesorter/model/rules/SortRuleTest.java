package com.fenrir.filesorter.model.rules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SortRuleTest {

    @Test
    public void resolveRuleWithoutFlags() {
        Rule rule = new SortRule("abcd");
        Rule.RuleElement element = rule.next();
        assertEquals("abcd", element.element());
        assertFalse(element.isToken());

        element = rule.next();
        assertNull(element);
    }

    @Test
    void resolveRuleContainingOneElement() {
        Rule rule = new SortRule("-");

        Rule.RuleElement element = rule.next();
        assertEquals("-", element.element());
        assertFalse(element.isToken());

        element = rule.next();
        assertNull(element);
    }

    @Test
    void resolveRuleContainingOnlyFlags() {
        Rule rule = new SortRule("%(YYYY)%(MM)%(/)%(DD)");

        Rule.RuleElement element = rule.next();
        assertEquals("YYYY", element.element());
        assertTrue(element.isToken());

        element = rule.next();
        assertEquals("MM", element.element());
        assertTrue(element.isToken());

        element = rule.next();
        assertEquals("/", element.element());
        assertTrue(element.isToken());

        element = rule.next();
        assertEquals("DD", element.element());
        assertTrue(element.isToken());

        element = rule.next();
        assertNull(element);
    }

    @Test
    public void resolveRuleWithFlagAtTheBeginning() {
        Rule rule = new SortRule("%(YYYY)-%(MM)%(/)%(DD)-");

        Rule.RuleElement element = rule.next();
        assertEquals("YYYY", element.element());
        assertTrue(element.isToken());

        element = rule.next();
        assertEquals("-", element.element());
        assertFalse(element.isToken());

        element = rule.next();
        assertEquals("MM", element.element());
        assertTrue(element.isToken());

        element = rule.next();
        assertEquals("/", element.element());
        assertTrue(element.isToken());

        element = rule.next();
        assertEquals("DD", element.element());
        assertTrue(element.isToken());

        element = rule.next();
        assertEquals("-", element.element());
        assertFalse(element.isToken());

        element = rule.next();
        assertNull(element);
    }

    @Test
    public void resolveRuleWithFlagAtTheEnd() {
        Rule rule = new SortRule("-%(YYYY)-%(MM)%(/)%(DD)");

        Rule.RuleElement element = rule.next();
        assertEquals("-", element.element());
        assertFalse(element.isToken());

        element = rule.next();
        assertEquals("YYYY", element.element());
        assertTrue(element.isToken());

        element = rule.next();
        assertEquals("-", element.element());
        assertFalse(element.isToken());

        element = rule.next();
        assertEquals("MM", element.element());
        assertTrue(element.isToken());

        element = rule.next();
        assertEquals("/", element.element());
        assertTrue(element.isToken());

        element = rule.next();
        assertEquals("DD", element.element());
        assertTrue(element.isToken());

        element = rule.next();
        assertNull(element);
    }

    @Test
    public void resolveRuleWithFlagsAtBothEnds() {
        Rule rule = new SortRule("%(YYYY)-%(MM)%(/)%(DD)");

        Rule.RuleElement element = rule.next();
        assertEquals("YYYY", element.element());
        assertTrue(element.isToken());

        element = rule.next();
        assertEquals("-", element.element());
        assertFalse(element.isToken());

        element = rule.next();
        assertEquals("MM", element.element());
        assertTrue(element.isToken());

        element = rule.next();
        assertEquals("/", element.element());
        assertTrue(element.isToken());

        element = rule.next();
        assertEquals("DD", element.element());
        assertTrue(element.isToken());

        element = rule.next();
        assertNull(element);
    }
}
