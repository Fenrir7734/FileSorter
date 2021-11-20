package com.fenrir.filesorter.model.parsers;

import com.fenrir.filesorter.model.exceptions.ArgumentFormatException;
import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.exceptions.TokenFormatException;
import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.rule.FilterRule;
import com.fenrir.filesorter.model.statement.predicate.PredicateOperands;
import com.fenrir.filesorter.model.statement.provider.FileNameProvider;
import com.fenrir.filesorter.model.statement.predicate.EqualPredicate;
import com.fenrir.filesorter.model.statement.predicate.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilterRuleParserTest {
    @TempDir
    Path tempDir;
    FileData file;
    FilterRuleParser parser;

    @BeforeEach
    public void init() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        file = new FileData(path);
        parser = new FilterRuleParser();
    }

    @Test
    public void shouldThrowExpressionFormatExceptionWhenOperatorTokenWasNotGiven() {
        FilterRule rule = new FilterRule("%(DAT)");
        ExpressionFormatException exception = assertThrows(
                ExpressionFormatException.class,
                () -> parser.resolveRule(rule),
                "Invalid expression, one of the mandatory tokens is missing."
        );
        assertEquals(rule, exception.getRule());
    }

    @Test
    public void shouldThrowExpressionFormatExceptionWhenOperandTokenWasNotGiven() {
        FilterRule rule = new FilterRule("%(<:12k)");
        ExpressionFormatException exception = assertThrows(
                ExpressionFormatException.class,
                () -> parser.resolveRule(rule),
                "Invalid expression, one of the mandatory tokens is missing."
        );
        assertEquals(rule, exception.getRule());
    }

    @Test
    public void shouldThrowExpressionFormatExceptionWhenGivenTooMuchTokens() {
        FilterRule rule = new FilterRule("%(SIZ)%(DAT)%(<:12k)");
        ExpressionFormatException exception = assertThrows(
                ExpressionFormatException.class,
                () -> parser.resolveRule(rule),
                "Filter expression should contain only operand and operator token."
        );
        assertEquals(rule, exception.getRule());
    }

    @Test
    public void shouldThrowExpressionFormatExceptionWhenGivenNonStringOperandToOnlyStringOperator() {
        FilterRule rule = new FilterRule("%(DAT)%(CON:2021-02-02)");
        ExpressionFormatException exception = assertThrows(
                ExpressionFormatException.class,
                () -> parser.resolveRule(rule),
                "Invalid type of operand for given operator"
        );
    }

    @Test
    public void shouldThrowTokenFormatExceptionWhenGivenInvalidOperatorToken() {
        FilterRule rule = new FilterRule("%(SIZ)%(B:12k)");
        TokenFormatException exception = assertThrows(
                TokenFormatException.class,
                () -> parser.resolveRule(rule),
                "Unknown operand."
        );
        assertEquals("B", exception.getToken());
        assertEquals(rule, exception.getRule());
    }

    @Test
    public void shouldThrowTokenFormatExceptionWhenGivenInvalidOperandToken() {
        FilterRule rule = new FilterRule("%(ABC)%(<:12k)");
        TokenFormatException exception = assertThrows(
                TokenFormatException.class,
                () -> parser.resolveRule(rule),
                "Unknown operand."
        );
        assertEquals("ABC", exception.getToken());
        assertEquals(rule, exception.getRule());
    }

    @Test
    public void shouldThrowTokenFormatExceptionWhenNoneArgumentOperatorContainsArguments() {

    }

    @Test
    public void shouldThrowTokenFormatExceptionWhenSingleArgumentOperatorContainsZeroArguments() {
        FilterRule rule = new FilterRule("%(SIZ)%(<:)");
        TokenFormatException exception = assertThrows(
                TokenFormatException.class,
                () -> parser.resolveRule(rule),
                "Expected only one argument"
        );
        assertEquals("<", exception.getToken());
        assertEquals(rule, exception.getRule());
    }

    @Test
    public void shouldThrowTokenFormatExceptionWhenSingleArgumentOperatorContainsMoreThanOneArgument() {
        FilterRule rule = new FilterRule("%(SIZ)%(<:12k, 15M)");
        TokenFormatException exception = assertThrows(
                TokenFormatException.class,
                () -> parser.resolveRule(rule),
                "Expected only one argument"
        );
        assertEquals("<", exception.getToken());
        assertEquals(rule, exception.getRule());
    }

    @Test
    public void shouldThrowTokenFormatExceptionWhenMultipleArgumentOperatorContainsZeroArguments() {
        FilterRule rule = new FilterRule("%(SIZ)%(==:)");
        TokenFormatException exception = assertThrows(
                TokenFormatException.class,
                () -> parser.resolveRule(rule),
                "Expected at least one argument."
        );
        assertEquals("==", exception.getToken());
        assertEquals(rule, exception.getRule());
    }

    @Test
    public void shouldThrowArgumentFormatExceptionWhenGivenInvalidArguments() {
        FilterRule rule = new FilterRule("%(SIZ)%(==:12k, 54M, 23F)");
        ArgumentFormatException exception = assertThrows(
                ArgumentFormatException.class,
                () -> parser.resolveRule(rule),
                "Incorrect size"
        );
        assertEquals("23F", exception.getArg());
        assertEquals("SIZ", exception.getToken());
        assertEquals(rule, exception.getRule());
    }

    @Test
    public void shouldReturnFilterOperatorStatementForValidInput() throws IOException, ExpressionFormatException {
        FilterRule rule = new FilterRule("%(FIN)%(==:testfile)");
        Predicate<? extends Comparable<?>> actualPredicate = parser.resolveRule(rule);
        assertTrue(actualPredicate instanceof EqualPredicate<?>);

        FileNameProvider operand = new FileNameProvider(null);
        List<String> args = List.of("testfile");
        PredicateOperands<String> operands = new PredicateOperands<>(operand, args);
        EqualPredicate<String> expectedPredicate = new EqualPredicate<>(operands);
        assertEquals(expectedPredicate.test(file), actualPredicate.test(file));
    }
}