package com.fenrir.filesorter.model.statement.predicate;

import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.provider.FileNameProvider;
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

class NotContainsPredicateTest {
    @TempDir
    Path tempDir;
    FileData file;

    @BeforeEach
    public void initFile() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        file = new FileData(path);
    }

    @Test
    public void shouldThrowExpressionFormatExceptionWhenGivenNonStringOperands() {
        Provider<Long> operand = new FileSizeProvider(null);
        List<Long> args = List.of(8L, 12L, 16L);
        PredicateOperands<Long> operands = new PredicateOperands<>(operand, args);
        assertThrows(
                ExpressionFormatException.class,
                () -> new NotContainsPredicate<>(operands, false),
                "Invalid type of operand for given operator"
        );
    }

    @Test
    public void testShouldReturnTrueIfOperandNotContainsAnyArgumentValue()
            throws IOException, ExpressionFormatException {
        Provider<String> operand = new FileNameProvider(null);
        List<String> args = List.of("abc", "bcd", "cde");
        PredicateOperands<String> operands = new PredicateOperands<>(operand, args);
        Predicate<String> predicate = new NotContainsPredicate<>(operands, false);
        assertTrue(predicate.test(file));
    }


    @Test
    public void testShouldReturnFalseIfOperandValueContainsAtLeastOneArgumentValue()
            throws IOException, ExpressionFormatException {
        Provider<String> operand = new FileNameProvider(null);
        List<String> args = List.of("abc", "bcd", "stfi");
        PredicateOperands<String> operands = new PredicateOperands<>(operand, args);
        Predicate<String> predicate = new NotContainsPredicate<>(operands, false);
        assertFalse(predicate.test(file));
    }

    @Test
    public void testShouldReturnWhenInversionModeIsEnabledAndIfOperandValueNotContainsAnyArgumentValue()
            throws IOException, ExpressionFormatException{
        Provider<String> operand = new FileNameProvider(null);
        List<String> args = List.of("abc", "bcd", "cde");
        PredicateOperands<String> operands = new PredicateOperands<>(operand, args);
        Predicate<String> predicate = new NotContainsPredicate<>(operands, true);
        assertFalse(predicate.test(file));
    }

    @Test
    public void testShouldReturnWhenInversionModeIsEnabledAndIfOperandValueContainsAtLeastOneArgumentValue()
            throws IOException, ExpressionFormatException {
        Provider<String> operand = new FileNameProvider(null);
        List<String> args = List.of("abc", "bcd", "stfi");
        PredicateOperands<String> operands = new PredicateOperands<>(operand, args);
        Predicate<String> predicate = new NotContainsPredicate<>(operands, true);
        assertTrue(predicate.test(file));
    }

}