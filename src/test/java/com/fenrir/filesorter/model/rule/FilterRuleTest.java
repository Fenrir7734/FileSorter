package com.fenrir.filesorter.model.rule;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilterRuleTest {

    @Test
    public void getRuleElementsIteratorShouldReturnIterator() {
        Rule rule = new StringRule("%(YYYY)%(MM)%(/)%(DD)");
        Iterator<RuleElement> iterator = rule.getRuleElementsIterator();
        assertNotNull(iterator);
    }

    @Test
    public void toStringShouldReturnExpression() {
        String expression = "%(YYYY)%(MM)%(/)%(DD)";
        Rule rule = new StringRule(expression);
        assertEquals(expression, rule.toString());
    }

    @Test
    public void resolveExpressionWithoutToken() {
        Rule rule = new FilterRule("abcd");
        Iterator<RuleElement> iterator = rule.getRuleElementsIterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    void resolveExpressionContainingOneToken() {
        Rule rule = new FilterRule("%(INC)");
        Iterator<RuleElement> iterator = rule.getRuleElementsIterator();

        RuleElement element = iterator.next();
        assertEquals("INC", element.element());
        assertTrue(element.isToken());
        assertNull(element.args());

        assertFalse(iterator.hasNext());
    }

    @Test
    void resolveExpressionContainingTokenWithArguments() {
        Rule rule = new FilterRule("%(==:123,456,789)");
        Iterator<RuleElement> iterator = rule.getRuleElementsIterator();

        RuleElement element = iterator.next();
        assertEquals("==", element.element());
        assertTrue(element.isToken());
        assertEquals(List.of("123", "456", "789"), element.args());

        assertFalse(iterator.hasNext());
    }

    @Test
    void resolveExpressionContainingLiterals() {
        Rule rule = new FilterRule("--%(INC)--");
        Iterator<RuleElement> iterator = rule.getRuleElementsIterator();

        RuleElement element = iterator.next();
        assertEquals("INC", element.element());
        assertTrue(element.isToken());
        assertNull(element.args());

        assertFalse(iterator.hasNext());
    }

    @Test
    void resolveExpressionForOperatorWithZeroArguments() {
        Rule rule = new FilterRule("%(<:)");
        Iterator<RuleElement> iterator = rule.getRuleElementsIterator();

        RuleElement element = iterator.next();
        assertEquals("<", element.element());
        assertTrue(element.isToken());
        assertNull(element.args());

        assertFalse(iterator.hasNext());
    }

    @Test
    void resolveExpressionForOperatorWithSpaceAsArgument() {
        Rule rule = new FilterRule("%(<:12k, ,54M)");
        Iterator<RuleElement> iterator = rule.getRuleElementsIterator();

        RuleElement element = iterator.next();
        assertTrue(element.isToken());
        List<String> expectedArgs = List.of("12k", "54M");
        assertEquals(expectedArgs, element.args());

        assertFalse(iterator.hasNext());
    }
}