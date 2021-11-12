package com.fenrir.filesorter.model.rule;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FilterRuleTest {

    @Test
    public void resolveRuleWithoutToken() {
        Rule rule = new FilterRule("abcd");
        Iterator<RuleElement> iterator = rule.getRuleElementsIterator();
        RuleElement element = iterator.next();
        assertNull(element);
    }

    @Test
    void resolveRuleContainingOneToken() {
        Rule rule = new FilterRule("%(INC)");
        Iterator<RuleElement> iterator = rule.getRuleElementsIterator();

        RuleElement element = iterator.next();
        assertEquals("INC", element.element());
        assertTrue(element.isToken());
        assertNull(element.args());

        element = iterator.next();
        assertNull(element);
    }

    @Test
    void resolveRuleContainingTokenWithArguments() {
        Rule rule = new FilterRule("%(==:123,456,789)");
        Iterator<RuleElement> iterator = rule.getRuleElementsIterator();

        RuleElement element = iterator.next();
        assertEquals("==", element.element());
        assertTrue(element.isToken());
        assertArrayEquals(new String[]{"123", "456", "789"}, element.args());

        element = iterator.next();
        assertNull(element);
    }

    @Test
    void resolveRuleContainingLiterals() {
        Rule rule = new FilterRule("--%(INC)--");
        Iterator<RuleElement> iterator = rule.getRuleElementsIterator();

        RuleElement element = iterator.next();
        assertEquals("INC", element.element());
        assertTrue(element.isToken());
        assertNull(element.args());

        element = iterator.next();
        assertNull(element);
    }
}