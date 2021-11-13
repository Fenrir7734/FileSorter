package com.fenrir.filesorter.model.statement.filter.operator;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.filter.FilterStatementDescription;
import com.fenrir.filesorter.model.statement.filter.operand.FileSizeOperandStatement;
import com.fenrir.filesorter.model.statement.filter.operand.FilterOperandStatement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class SmallerEqualStatementTest {
    @TempDir
    Path tempDir;
    FileData file;

    @BeforeEach
    public void initFile() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        file = new FileData(path);
    }

    @Test
    public void shouldReturnPredicate() {
        FilterStatementDescription<String> description = new FilterStatementDescription<>(null, List.of("abc"));
        FilterOperatorStatement<String> operator = new SmallerEqualStatement<>(description);
        Predicate<FileData> predicate = operator.execute();
        assertNotNull(predicate);
    }

    @Test
    public void shouldIgnoreMoreThanOneArgument() {
        FilterOperandStatement<Long> operand = new FileSizeOperandStatement();
        List<Long> args = List.of(16L, 12L, 8L);
        FilterStatementDescription<Long> description = new FilterStatementDescription<>(operand, args);
        FilterOperatorStatement<Long> operator = new SmallerEqualStatement<>(description);
        Predicate<FileData> predicate = operator.execute();
        assertTrue(predicate.test(file));
    }

    @Test
    public void predicateShouldReturnTrueForOperandValueSmallerThanArgument() {
        FilterOperandStatement<Long> operand = new FileSizeOperandStatement();
        List<Long> args = List.of(16L);
        FilterStatementDescription<Long> description = new FilterStatementDescription<>(operand, args);
        FilterOperatorStatement<Long> operator = new SmallerEqualStatement<>(description);
        Predicate<FileData> predicate = operator.execute();
        assertTrue(predicate.test(file));
    }

    @Test
    public void predicateShouldReturnTrueForOperandValueEqualArgument() {
        FilterOperandStatement<Long> operand = new FileSizeOperandStatement();
        List<Long> args = List.of(12L);
        FilterStatementDescription<Long> description = new FilterStatementDescription<>(operand, args);
        FilterOperatorStatement<Long> operator = new SmallerEqualStatement<>(description);
        Predicate<FileData> predicate = operator.execute();
        assertTrue(predicate.test(file));
    }

    @Test
    public void predicateShouldReturnFalseForOperandValueGreaterThanArgument() {
        FilterOperandStatement<Long> operand = new FileSizeOperandStatement();
        List<Long> args = List.of(8L);
        FilterStatementDescription<Long> description = new FilterStatementDescription<>(operand, args);
        FilterOperatorStatement<Long> operator = new SmallerEqualStatement<>(description);
        Predicate<FileData> predicate = operator.execute();
        assertFalse(predicate.test(file));
    }
}