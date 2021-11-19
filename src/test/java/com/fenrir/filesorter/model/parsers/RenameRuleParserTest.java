package com.fenrir.filesorter.model.parsers;

import com.fenrir.filesorter.model.exceptions.TokenFormatException;
import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.rule.StringRule;
import com.fenrir.filesorter.model.statement.ProviderDescription;
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
    public void shouldThrowTokenFormatExceptionWhenGivenInvalidToken() {
        StringRule rule = new StringRule("%(ABC)");
        TokenFormatException exception = assertThrows(
                TokenFormatException.class,
                () -> parser.resolveRule(rule),
                "Unknown token."
        );
        assertEquals("ABC", exception.getToken());
        assertEquals(rule, exception.getRule());
    }

    @Test
    public void shouldThrowTokenFormatExceptionWhenGivenOnlySortToken() {
        StringRule rule = new StringRule("%(/)");
        TokenFormatException exception = assertThrows(
                TokenFormatException.class,
                () -> parser.resolveRule(rule),
                "Unknown token."
        );
        assertEquals("/", exception.getToken());
        assertEquals(rule, exception.getRule());
    }

    @Test
    public void shouldReturnListOfStringStatementForValidInput() throws TokenFormatException, IOException {
        StringRule stringRule = new StringRule("%(CUR)-%(YYYY)-%(MM)-%(DD).%(EXT)");
        List<Provider<?>> statementsFromParser = parser.resolveRule(stringRule);
        List<Provider<?>> expectedStatements = List.of(
                new FileNameProvider(null),
                new LiteralProvider(ProviderDescription.ofLiteral("-")),
                new DateProvider(ProviderDescription.ofDate("YYYY")),
                new LiteralProvider(ProviderDescription.ofLiteral("-")),
                new DateProvider(ProviderDescription.ofDate("MMM")),
                new LiteralProvider(ProviderDescription.ofLiteral("-")),
                new DateProvider(ProviderDescription.ofDate("DD")),
                new LiteralProvider(ProviderDescription.ofLiteral(".")),
                new FileExtensionProvider(null)
        );
        String actualResult = StatementUtils.build(statementsFromParser, file);
        String expectedResult = StatementUtils.build(expectedStatements, file);
        assertEquals(expectedResult, actualResult);
    }
}