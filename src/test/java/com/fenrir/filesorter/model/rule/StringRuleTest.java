package com.fenrir.filesorter.model.rule;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringRuleTest {

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
    public void resolveExpressionWithoutTokens() {
        Rule rule = new StringRule("abcd");
        Iterator<RuleElement> iterator = rule.getRuleElementsIterator();

        RuleElement element = iterator.next();
        assertEquals("abcd", element.element());
        assertFalse(element.isToken());

        element = iterator.next();
        assertNull(element);
    }

    @Test
    void resolveExpressionContainingOneElement() {
        Rule rule = new StringRule("-");
        Iterator<RuleElement> iterator = rule.getRuleElementsIterator();

        RuleElement element = iterator.next();
        assertEquals("-", element.element());
        assertFalse(element.isToken());

        element = iterator.next();
        assertNull(element);
    }

    @Test
    void resolveExpressionContainingOnlyTokens() {
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
    public void resolveExpressionWithTokenAtTheBeginning() {
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
    public void resolveExpressionWithTokenAtTheEnd() {
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
    public void resolveExpressionWithTokensAtBothEnds() {
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
