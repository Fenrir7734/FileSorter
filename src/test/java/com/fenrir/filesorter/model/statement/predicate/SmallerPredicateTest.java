package com.fenrir.filesorter.model.statement.predicate;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.filter.FilterStatementDescription;
import com.fenrir.filesorter.model.statement.provider.FileSizeProvider;
import com.fenrir.filesorter.model.statement.provider.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SmallerStatementTest {
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
        FilterStatementDescription<String> description = new FilterStatementDescription<>(null, List.of("abc"));
        Predicate<String> operator = new SmallerPredicate<>(description);
        java.util.function.Predicate predicate = operator.execute();
        assertNotNull(predicate);
    }

    @Test
    public void shouldIgnoreMoreThanOneArgument() {
        Provider<Long> operand = new FileSizeProvider(null);
        List<Long> args = List.of(16L, 12L, 8L);
        FilterStatementDescription<Long> description = new FilterStatementDescription<>(operand, args);
        Predicate<Long> operator = new SmallerPredicate<>(description);
        java.util.function.Predicate predicate = operator.execute();
        assertTrue(predicate.test(file));
    }

    @Test
    public void predicateShouldReturnTrueForOperandValueSmallerThanArgument() {
        Provider<Long> operand = new FileSizeProvider(null);
        List<Long> args = List.of(16L);
        FilterStatementDescription<Long> description = new FilterStatementDescription<>(operand, args);
        Predicate<Long> operator = new SmallerPredicate<>(description);
        java.util.function.Predicate predicate = operator.execute();
        assertTrue(predicate.test(file));
    }

    @Test
    public void predicateShouldReturnFalseForOperandValueEqualArgument() {
        Provider<Long> operand = new FileSizeProvider(null);
        List<Long> args = List.of(12L);
        FilterStatementDescription<Long> description = new FilterStatementDescription<>(operand, args);
        Predicate<Long> operator = new SmallerPredicate<>(description);
        java.util.function.Predicate predicate = operator.execute();
        assertFalse(predicate.test(file));
    }

    @Test
    public void predicateShouldReturnFalseForOperandValueGreaterThanArgument() {
        Provider<Long> operand = new FileSizeProvider(null);
        List<Long> args = List.of(8L);
        FilterStatementDescription<Long> description = new FilterStatementDescription<>(operand, args);
        Predicate<Long> operator = new SmallerPredicate<>(description);
        java.util.function.Predicate predicate = operator.execute();
        assertFalse(predicate.test(file));
    }
}