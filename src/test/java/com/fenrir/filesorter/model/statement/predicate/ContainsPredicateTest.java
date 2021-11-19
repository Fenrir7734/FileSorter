package com.fenrir.filesorter.model.statement.predicate;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.filter.FilterStatementDescription;
import com.fenrir.filesorter.model.statement.provider.FileNameProvider;
import com.fenrir.filesorter.model.statement.provider.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContainsPredicateTest {
    @TempDir
    Path tempDir;
    FileData file;

    @BeforeEach
    public void initFile() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        file = new FileData(path);
    }

    @Test
    public void executeShouldReturnPredicate() {
        FilterStatementDescription<String> description = new FilterStatementDescription<>(null, null);
        Predicate<String> operator = new ContainsPredicate(description);
        java.util.function.Predicate predicate = operator.execute();
        assertNotNull(predicate);
    }

    @Test
    public void predicateShouldReturnTrueIfOperandValueContainsAtLeastOneArgumentValue() {
        Provider<String> operand = new FileNameProvider(null);
        List<String> args = List.of("abc", "bcd", "stfi");
        FilterStatementDescription<String> description = new FilterStatementDescription<>(operand, args);
        Predicate<String> operator = new ContainsPredicate(description);
        java.util.function.Predicate predicate = operator.execute();
        assertTrue(predicate.test(file));
    }

    @Test
    public void predicateShouldReturnFalseIfOperandNotContainsAnyArgumentValue() {
        Provider<String> operand = new FileNameProvider(null);
        List<String> args = List.of("abc", "bcd", "cde");
        FilterStatementDescription<String> description = new FilterStatementDescription<>(operand, args);
        Predicate<String> operator = new ContainsPredicate(description);
        java.util.function.Predicate predicate = operator.execute();
        assertFalse(predicate.test(file));
    }
}