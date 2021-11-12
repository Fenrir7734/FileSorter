package com.fenrir.filesorter.model.rule;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FilterRuleTest {

    @Test
    public void resolveRuleWithoutToken() {
        Rule rule = new FilterRule("abcd");
        Rule.RuleElement element = rule.next();
        assertNull(element);
    }

    @Test
    void resolveRuleContainingOneToken() {
        Rule rule = new FilterRule("%(INC)");

        Rule.RuleElement element = rule.next();
        assertEquals("INC", element.element());
        assertTrue(element.isToken());
        assertNull(element.args());

        element = rule.next();
        assertNull(element);
    }

    @Test
    void resolveRuleContainingTokenWithArguments() {
        Rule rule = new FilterRule("%(==:123,456,789)");

        Rule.RuleElement element = rule.next();
        assertEquals("==", element.element());
        assertTrue(element.isToken());
        assertArrayEquals(new String[]{"123", "456", "789"}, element.args());

        element = rule.next();
        assertNull(element);
    }

    @Test
    void resolveRuleContainingLiterals() {
        Rule rule = new FilterRule("--%(INC)--");

        Rule.RuleElement element = rule.next();
        assertEquals("INC", element.element());
        assertTrue(element.isToken());
        assertNull(element.args());

        element = rule.next();
        assertNull(element);
    }
}