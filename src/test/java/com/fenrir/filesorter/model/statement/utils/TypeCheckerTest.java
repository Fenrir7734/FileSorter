package com.fenrir.filesorter.model.statement.utils;

import com.fenrir.filesorter.model.statement.predicate.PredicateOperands;
import com.fenrir.filesorter.model.statement.provider.FileNameProvider;
import com.fenrir.filesorter.model.statement.provider.FileSizeProvider;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TypeCheckerTest {
    @Test
    public void isInstanceOfStringShouldReturnTrueIfGivenArgumentIsOfTypeString() {
        PredicateOperands<String> operands = new PredicateOperands<>(
                new FileNameProvider(null),
                List.of("a", "b", "c")
        );
        assertTrue(TypeChecker.isInstanceOfString(operands));
    }

    @Test
    public void isInstanceOfStringShouldReturnFalseIfGivenArgumentIsNotOfTypeString() {
        PredicateOperands<Long> operands = new PredicateOperands<>(
                new FileSizeProvider(null),
                List.of(8L, 12L, 16L)
        );
        assertFalse(TypeChecker.isInstanceOfString(operands));
    }
}