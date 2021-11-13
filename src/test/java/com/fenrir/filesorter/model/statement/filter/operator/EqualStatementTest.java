package com.fenrir.filesorter.model.statement.filter.operator;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.filter.FilterStatementDescription;
import com.fenrir.filesorter.model.statement.filter.operand.FileNameOperandStatement;
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

class EqualStatementTest {
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
        FilterOperatorStatement<String> operator = new EqualStatement<>(description);
        Predicate<FileData> predicate = operator.execute();
        assertNotNull(predicate);
    }

    @Test
    public void predicateShouldReturnTrue() {
        FilterOperandStatement<String> operand = new FileNameOperandStatement();
        List<String> args = List.of("file", "test", "testfile");
        FilterStatementDescription<String> description = new FilterStatementDescription<>(operand, args);
        FilterOperatorStatement<String> operator = new EqualStatement<>(description);
        Predicate<FileData> predicate = operator.execute();
        assertTrue(predicate.test(file));
    }

    @Test
    public void predicateShouldReturnFalse() {
        FilterOperandStatement<String> operand = new FileNameOperandStatement();
        List<String> args = List.of("file", "test", "file2");
        FilterStatementDescription<String> description = new FilterStatementDescription<>(operand, args);
        FilterOperatorStatement<String> operator = new EqualStatement<>(description);
        Predicate<FileData> predicate = operator.execute();
        assertFalse(predicate.test(file));
    }
}