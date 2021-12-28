package com.fenrir.filesorter.model.parsers;

import com.fenrir.filesorter.model.exceptions.ArgumentFormatException;
import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.exceptions.TokenFormatException;
import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.rule.Rule;
import com.fenrir.filesorter.model.statement.provider.ProviderDescription;
import com.fenrir.filesorter.model.statement.provider.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;
import utils.StatementUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RenameRuleParserTest {
    @TempDir
    Path tempDir;
    FileData file;
    RenameRuleParser parser;

    @BeforeEach
    public void init() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        file = new FileData(path);
        parser = new RenameRuleParser();
    }

    @Test
    public void shouldThrowTokenFormatExceptionWhenGivenInvalidToken() throws ExpressionFormatException {
        Rule rule = new Rule("%(ABC)");
        TokenFormatException exception = assertThrows(
                TokenFormatException.class,
                () -> parser.resolveRule(rule),
                "Unknown token."
        );
        assertEquals("ABC", exception.getToken());
        assertEquals(rule, exception.getRule());
    }

    @Test
    public void shouldThrowTokenFormatExceptionWhenGivenSortToken() throws ExpressionFormatException {
        Rule rule = new Rule("%(/)");
        TokenFormatException exception = assertThrows(
                TokenFormatException.class,
                () -> parser.resolveRule(rule),
                "Unknown token."
        );
        assertEquals("/", exception.getToken());
        assertEquals(rule, exception.getRule());
    }

    @Test
    public void shouldThrowTokenFormatExceptionWhenNoneArgumentTokenContainsArguments()
            throws ExpressionFormatException {
        Rule rule = new Rule("%(DIM:1920x1080)");
        TokenFormatException exception = assertThrows(
                TokenFormatException.class,
                () -> parser.resolveRule(rule),
                "Expected zero arguments"
        );
        assertEquals("DIM", exception.getToken());
        assertEquals(rule, exception.getRule());
    }

    @Test
    public void shouldThrowTokenFormatExceptionWhenSingleArgumentOperatorContainsZeroArguments()
            throws ExpressionFormatException {
        Rule rule = new Rule("%(STR:)");
        TokenFormatException exception = assertThrows(
                TokenFormatException.class,
                () -> parser.resolveRule(rule),
                "Expected only one argument"
        );
        assertEquals("STR", exception.getToken());
        assertEquals(rule, exception.getRule());
    }

    @Test
    public void shouldThrowTokenFormatExceptionWhenSingleArgumentOperatorContainsMoreThanOneArgument()
            throws ExpressionFormatException {
        Rule rule = new Rule("%(STR:abc, dbc)");
        TokenFormatException exception = assertThrows(
                TokenFormatException.class,
                () -> parser.resolveRule(rule),
                "Expected only one argument"
        );
        assertEquals("STR", exception.getToken());
        assertEquals(rule, exception.getRule());
    }

    @Test
    public void shouldThrowArgumentFormatExceptionWhenGiveInvalidArgument() throws ExpressionFormatException {
        Rule rule = new Rule("%(DAC: INVALID)");
        ArgumentFormatException exception = assertThrows(
                ArgumentFormatException.class,
                () -> parser.resolveRule(rule),
                "Invalid date format"
        );
        assertEquals(" INVALID", exception.getArg());
        assertEquals("DAC", exception.getToken());
        assertEquals(rule, exception.getRule());
    }

    @Test
    public void shouldReturnListOfStringStatementForValidInput() throws ExpressionFormatException, IOException {
        Rule stringRule = new Rule("%(FIN)%(TXT:-)%(DAC:YYYY)%(TXT:-)%(DAC:MM)%(TXT:-)%(DAC:DD)%(TXT:.)%(EXT)");
        List<Provider<?>> statementsFromParser = parser.resolveRule(stringRule);
        List<Provider<?>> expectedStatements = List.of(
                new FileNameProvider(null),
                new CustomTextProvider(ProviderDescription.ofLiteral("-")),
                new DateCreatedProvider(ProviderDescription.ofDate("YYYY")),
                new CustomTextProvider(ProviderDescription.ofLiteral("-")),
                new DateCreatedProvider(ProviderDescription.ofDate("MM")),
                new CustomTextProvider(ProviderDescription.ofLiteral("-")),
                new DateCreatedProvider(ProviderDescription.ofDate("DD")),
                new CustomTextProvider(ProviderDescription.ofLiteral(".")),
                new FileExtensionProvider(null)
        );
        String actualResult = StatementUtils.build(statementsFromParser, file);
        String expectedResult = StatementUtils.build(expectedStatements, file);
        assertEquals(expectedResult, actualResult);
    }
}