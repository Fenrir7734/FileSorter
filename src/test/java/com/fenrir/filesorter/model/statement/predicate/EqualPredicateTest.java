package com.fenrir.filesorter.model.statement.predicate;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.PredicateOperands;
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

class EqualPredicateTest {
    @TempDir
    Path tempDir;
    FileData file;

    @BeforeEach
    public void initFile() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        file = new FileData(path);
    }

    @Test
    public void testShouldReturnTrue() throws IOException {
        Provider<String> operand = new FileNameProvider(null);
        List<String> args = List.of("file", "test", "testfile");
        PredicateOperands<String> operands = new PredicateOperands<>(operand, args);
        Predicate<String> predicate = new EqualPredicate<>(operands);
        assertTrue(predicate.test(file));
    }

    @Test
    public void testShouldReturnFalse() throws IOException {
        Provider<String> operand = new FileNameProvider(null);
        List<String> args = List.of("file", "test", "file2");
        PredicateOperands<String> operands = new PredicateOperands<>(operand, args);
        Predicate<String> predicate = new EqualPredicate<>(operands);
        assertFalse(predicate.test(file));
    }
}