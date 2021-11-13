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
    public void testResolveExpressionForExpressionWithTokens() {
        Rule rule = new StringRule("abcd");
        Iterator<RuleElement> iterator = rule.getRuleElementsIterator();

        RuleElement element = iterator.next();
        assertEquals("abcd", element.element());
        assertFalse(element.isToken());

        assertFalse(iterator.hasNext());
    }

    @Test
    void testResolveExpressionForExpressionWithOneElement() {
        Rule rule = new StringRule("-");
        Iterator<RuleElement> iterator = rule.getRuleElementsIterator();

        RuleElement element = iterator.next();
        assertEquals("-", element.element());
        assertFalse(element.isToken());

        assertFalse(iterator.hasNext());
    }

    @Test
    void testResolveExpressionForExpressionWithOnlyTokens() {
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

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testResolveExpressionForExpressionWithTokenAtTheBeginning() {
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

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testResolveExpressionForExpressionWithTokenAtTheEnd() {
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

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testResolveExpressionForExpressionWithTokensAtBothEnds() {
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

        assertFalse(iterator.hasNext());
    }
}
