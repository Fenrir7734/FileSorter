package com.fenrir.filesorter.model.statement.predicate;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.PredicateOperands;
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

class GreaterEqualPredicateTest {
    @TempDir
    Path tempDir;
    FileData file;

    @BeforeEach
    public void initFile() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        file = new FileData(path);
    }

    @Test
    public void shouldIgnoreMoreThanOneArgument() throws IOException {
        Provider<Long> operand = new FileSizeProvider(null);
        List<Long> args = List.of(8L, 12L, 16L);
        PredicateOperands<Long> operands = new PredicateOperands<>(operand, args);
        Predicate<Long> predicate = new GreaterEqualPredicate<>(operands);
        assertTrue(predicate.test(file));
    }

    @Test
    public void testShouldReturnTrueForOperandValueGreaterThanArgument() throws IOException {
        Provider<Long> operand = new FileSizeProvider(null);
        List<Long> args = List.of(8L);
        PredicateOperands<Long> operands = new PredicateOperands<>(operand, args);
        Predicate<Long> predicate = new GreaterEqualPredicate<>(operands);
        assertTrue(predicate.test(file));
    }

    @Test
    public void testShouldReturnTrueForOperandValueEqualArgument() throws IOException {
        Provider<Long> operand = new FileSizeProvider(null);
        List<Long> args = List.of(12L);
        PredicateOperands<Long> operands = new PredicateOperands<>(operand, args);
        Predicate<Long> predicate = new GreaterEqualPredicate<>(operands);
        assertTrue(predicate.test(file));
    }

    @Test
    public void testShouldReturnFalseForOperandValueSmallerThanArgument() throws IOException {
        Provider<Long> operand = new FileSizeProvider(null);
        List<Long> args = List.of(16L);
        PredicateOperands<Long> operands = new PredicateOperands<>(operand, args);
        Predicate<Long> predicate = new GreaterEqualPredicate<>(operands);
        assertFalse(predicate.test(file));
    }
}