package com.fenrir.filesorter.model.parsers;

import com.fenrir.filesorter.model.exceptions.TokenFormatException;
import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.rule.StringRule;
import com.fenrir.filesorter.model.statement.string.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

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
        List<StringStatement> statementsFromParser = parser.resolveRule(stringRule);
        List<StringStatement> expectedStatements = List.of(
                new FileNameStatement(null),
                new LiteralStatement(new StringStatementDescription(null, "-")),
                new DateStatement(new StringStatementDescription("YYYY", null)),
                new LiteralStatement(new StringStatementDescription(null, "-")),
                new DateStatement(new StringStatementDescription("MMM", null)),
                new LiteralStatement(new StringStatementDescription(null, "-")),
                new DateStatement(new StringStatementDescription("DD", null)),
                new LiteralStatement(new StringStatementDescription(null, ".")),
                new FileExtensionStatement(null)
        );
        String actualResult = Utils.build(statementsFromParser, file);
        String expectedResult = Utils.build(expectedStatements, file);
        assertEquals(expectedResult, actualResult);
    }
}