package com.fenrir.filesorter.model.statement.predicate;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.provider.FileSizeProvider;
import com.fenrir.filesorter.model.statement.provider.Provider;
import com.fenrir.filesorter.model.statement.types.ActionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SmallerEqualPredicateTest {
    @TempDir
    Path tempDir;
    FileData file;

    @BeforeEach
    public void initFile() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        file = new FileData(path);
    }

    @Test
    public void shouldIgnoreMoreThanOneArgument()
            throws IOException {
        Provider<Long> operand = new FileSizeProvider(null);
        List<Long> args = List.of(16L, 12L, 8L);
        PredicateOperands<Long> operands = new PredicateOperands<>(operand, args);
        Predicate<Long> predicate = new SmallerEqualPredicate<>(ActionType.INCLUDE, operands);
        assertTrue(predicate.test(file));
    }

    @Test
    public void testShouldReturnTrueForIncludeActionIfOperandValueIsSmallerThanArgumentValue()
            throws IOException {
        Provider<Long> operand = new FileSizeProvider(null);
        List<Long> args = List.of(16L);
        PredicateOperands<Long> operands = new PredicateOperands<>(operand, args);
        Predicate<Long> predicate = new SmallerEqualPredicate<>(ActionType.INCLUDE, operands);
        assertTrue(predicate.test(file));
    }

    @Test
    public void testShouldReturnTrueForIncludeActionIfOperandValueIsEqualArgumentValue()
            throws IOException {
        Provider<Long> operand = new FileSizeProvider(null);
        List<Long> args = List.of(12L);
        PredicateOperands<Long> operands = new PredicateOperands<>(operand, args);
        Predicate<Long> predicate = new SmallerEqualPredicate<>(ActionType.INCLUDE, operands);
        assertTrue(predicate.test(file));
    }

    @Test
    public void testShouldReturnFalseForIncludeActionIfOperandValueIsGreaterThanArgumentValue()
            throws IOException {
        Provider<Long> operand = new FileSizeProvider(null);
        List<Long> args = List.of(8L);
        PredicateOperands<Long> operands = new PredicateOperands<>(operand, args);
        Predicate<Long> predicate = new SmallerEqualPredicate<>(ActionType.INCLUDE, operands);
        assertFalse(predicate.test(file));
    }

    @Test
    public void testShouldReturnFalseForExcludeActionIfOperandValueIsSmallerThanArgumentValue()
            throws IOException {
        Provider<Long> operand = new FileSizeProvider(null);
        List<Long> args = List.of(16L);
        PredicateOperands<Long> operands = new PredicateOperands<>(operand, args);
        Predicate<Long> predicate = new SmallerEqualPredicate<>(ActionType.EXCLUDE, operands);
        assertFalse(predicate.test(file));
    }

    @Test
    public void testShouldReturnFalseForExcludeActionIfOperandValueIsEqualArgumentValue()
            throws IOException {
        Provider<Long> operand = new FileSizeProvider(null);
        List<Long> args = List.of(12L);
        PredicateOperands<Long> operands = new PredicateOperands<>(operand, args);
        Predicate<Long> predicate = new SmallerEqualPredicate<>(ActionType.EXCLUDE, operands);
        assertFalse(predicate.test(file));
    }

    @Test
    public void testShouldReturnTrueForExcludeActionIfOperandValueIsGreaterThanArgumentValue()
            throws IOException {
        Provider<Long> operand = new FileSizeProvider(null);
        List<Long> args = List.of(8L);
        PredicateOperands<Long> operands = new PredicateOperands<>(operand, args);
        Predicate<Long> predicate = new SmallerEqualPredicate<>(ActionType.EXCLUDE, operands);
        assertTrue(predicate.test(file));
    }
}