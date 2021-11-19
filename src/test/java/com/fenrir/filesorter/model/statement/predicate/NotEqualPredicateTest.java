package com.fenrir.filesorter.model.statement.predicate;

import com.fenrir.filesorter.model.file.FileData;
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

class NotEqualPredicateTest {
    @TempDir
    Path tempDir;
    FileData file;

    @BeforeEach
    public void initFile() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        file = new FileData(path);
    }

    @Test
    public void testShouldReturnTrueIfOperandValueNotEqualToAnyArgumentValue() throws IOException {
        Provider<String> operand = new FileNameProvider(null);
        List<String> args = List.of("abc", "cbd", "def");
        PredicateOperands<String> operands = new PredicateOperands<>(operand, args);
        Predicate<String> predicate = new NotEqualPredicate<>(operands);
        assertTrue(predicate.test(file));
    }

    @Test
    public void testShouldReturnFalseIfOperandValueEqualsToAtLeastOneArgumentValue() throws IOException {
        Provider<String> operand = new FileNameProvider(null);
        List<String> args = List.of("abc", "cbd", "testfile");
        PredicateOperands<String> operands = new PredicateOperands<>(operand, args);
        Predicate<String> predicate = new NotEqualPredicate<>(operands);
        assertFalse(predicate.test(file));
    }
}