package com.fenrir.filesorter.model.rule;

import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RuleTest {
    @Test
    public void getTokenIteratorShouldReturnIterator() throws ExpressionFormatException {
        Rule rule = new Rule("%(DIM)%(FIN)%(/)%(CAT)");
        Iterator<Token> iterator = rule.getTokenIterator();
        assertNotNull(iterator);
    }

    @Test
    public void toStringShouldReturnExpression() throws ExpressionFormatException {
        String expression = "%(DIM)%(FIN)%(/)%(CAT)";
        Rule rule = new Rule("%(DIM)%(FIN)%(/)%(CAT)");
        assertEquals(expression, rule.toString());
    }

    @Test
    public void shouldThrowExpressionFormatExceptionForExpressionWithoutTokens() {
        String expression = "abc";
        assertThrows(
            ExpressionFormatException.class,
                () -> new Rule(expression),
                "Invalid expression"
        );
    }

    @Test
    public void shouldThrowExpressionFormatExceptionForExpressionWithLiteralAtTheBeginningOfTheExpression() {
        String expression = "abc%(DIM)%(FIN)%(/)%(CAT)";
        assertThrows(
                ExpressionFormatException.class,
                () -> new Rule(expression),
                "Invalid expression"
        );
    }

    @Test
    public void shouldThrowExpressionFormatExceptionForExpressionWithLiteralInTheMiddleOfTheExpression() {
        String expression = "%(DIM)%(FIN)%(/)abc%(CAT)";
        assertThrows(
                ExpressionFormatException.class,
                () -> new Rule(expression),
                "Invalid expression"
        );
    }

    @Test
    public void shouldThrowExpressionFormatExceptionForExpressionWithLiteralAtTheEndOfTheExpression() {
        String expression = "%(DIM)%(FIN)%(/)%(CAT)abc";
        assertThrows(
                ExpressionFormatException.class,
                () -> new Rule(expression),
                "Invalid expression"
        );
    }

    @Test
    public void shouldThrowExpressionFormatExceptionForExpressionWithNestedTokens() {
        String expression = "%(%(DIM))";
        assertThrows(
                ExpressionFormatException.class,
                () -> new Rule(expression),
                "Invalid expression"
        );
    }

    @Test
    public void shouldResolveExpressionWithOneToken() throws ExpressionFormatException {
        Rule rule = new Rule("%(INC)");
        Iterator<Token> iterator = rule.getTokenIterator();

        Token token = iterator.next();
        assertEquals("INC", token.symbol());
        assertNull(token.args());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void shouldResolveExpressionWithTokenWithArguments() throws ExpressionFormatException {
        Rule rule = new Rule("%(==:123,456,789)");
        Iterator<Token> iterator = rule.getTokenIterator();

        Token token = iterator.next();
        assertEquals("==", token.symbol());
        assertEquals(List.of("123", "456", "789"), token.args());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void shouldResolveExpressionWithTokenWithZeroArguments() throws ExpressionFormatException {
        Rule rule = new Rule("%(<:)");
        Iterator<Token> iterator = rule.getTokenIterator();

        Token token = iterator.next();
        assertEquals("<", token.symbol());
        assertNull(token.args());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void shouldResolveExpressionWithTokenWithSpaceAsArgument() throws ExpressionFormatException {
        Rule rule = new Rule("%(<:12k, ,54M)");
        Iterator<Token> iterator = rule.getTokenIterator();

        Token token = iterator.next();
        assertEquals("<", token.symbol());
        assertEquals(List.of("12k", "54M"), token.args());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void shouldResolveExpressionWithTokensWithoutMultipleTokens() throws ExpressionFormatException {
        Rule rule = new Rule("%(DIM)%(FIN)%(/)%(DAT:YYYY-0M-0D)");
        Iterator<Token> iterator = rule.getTokenIterator();

        Token token = iterator.next();
        assertEquals("DIM", token.symbol());
        assertNull(token.args());

        token = iterator.next();
        assertEquals("FIN", token.symbol());
        assertNull(token.args());

        token = iterator.next();
        assertEquals("/", token.symbol());
        assertNull(token.args());

        token = iterator.next();
        assertEquals("DAT", token.symbol());
        assertEquals(List.of("YYYY-0M-0D"), token.args());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void equalShouldReturnTrueIfObjectsAreTheSame() throws ExpressionFormatException {
        Rule rule1 = new Rule("%(DIM)%(FIN)%(/)%(DAT:YYYY-0M-0D)");
        Rule rule2 = new Rule("%(DIM)%(FIN)%(/)%(DAT:YYYY-0M-0D)");
        assertEquals(rule1, rule2);
    }

    @Test
    public void equalShouldReturnFalseIfExpressionsAreDifferent() throws ExpressionFormatException {
        Rule rule1 = new Rule("%(DIM)%(FIN)%(/)%(DAT:YYYY-0M-0D)");
        Rule rule2 = new Rule("%(DIM)%(FIN)");
        assertNotEquals(rule1, rule2);
    }

    @Test
    public void equalShouldReturnFalseIfOneObjectIsEqualNull() throws ExpressionFormatException {
        Rule rule1 = new Rule("%(DIM)%(FIN)%(/)%(DAT:YYYY-0M-0D)");
        assertNotEquals(rule1, null);
    }

    @Test
    public void equalShouldReturnFalseForObjectsOfADifferentType() throws ExpressionFormatException {
        Rule rule1 = new Rule("%(DIM)%(FIN)%(/)%(DAT:YYYY-0M-0D)");
        assertNotEquals(rule1, new Object());
    }
}
