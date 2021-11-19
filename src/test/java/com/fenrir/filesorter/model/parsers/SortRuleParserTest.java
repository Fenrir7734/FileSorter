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

class SortRuleParserTest {
    @TempDir
    Path tempDir;
    FileData file;
    SortRuleParser parser;

    @BeforeEach
    public void init() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        file = new FileData(path);
        parser = new SortRuleParser();
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
    public void shouldThrowTokenFormatExceptionWhenGivenOnlyRenameToken() {
        StringRule rule = new StringRule("%(CUR)");
        TokenFormatException exception = assertThrows(
                TokenFormatException.class,
                () -> parser.resolveRule(rule),
                "Unknown token."
        );
        assertEquals("CUR", exception.getToken());
        assertEquals(rule, exception.getRule());
    }

    @Test
    public void shouldReturnListOfStringStatementForValidInput() throws TokenFormatException, IOException {
        StringRule stringRule = new StringRule("%(YYYY)%(MM)%(/)%(DD)-%(EXT)");
        List<Provider<?>> statementsFromParser = parser.resolveRule(stringRule);
        List<Provider<?>> expectedStatements = List.of(
                new DateProvider(ProviderDescription.ofDate("YYYY")),
                new DateProvider(ProviderDescription.ofDate("MMM")),
                new FileSeparatorProvider(null),
                new DateProvider(ProviderDescription.ofDate("DD")),
                new LiteralProvider(ProviderDescription.ofLiteral("-")),
                new FileExtensionProvider(null)
        );
        String actualResult = StatementUtils.build(statementsFromParser, file);
        String expectedResult = StatementUtils.build(expectedStatements, file);
        assertEquals(expectedResult, actualResult);
    }
}