package com.fenrir.filesorter.model.statement.types;

import com.fenrir.filesorter.model.enums.Scope;
import com.fenrir.filesorter.model.exceptions.ArgumentFormatException;
import com.fenrir.filesorter.model.statement.predicate.PredicateOperands;
import com.fenrir.filesorter.model.statement.provider.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProviderTypeTest {
    @Test
    public void getTypeShouldReturnCorrectProviderTypeForValidTokenAndScope() {
        ProviderType actualType = ProviderType.getType("DIM", Scope.RENAME);
        assertEquals(ProviderType.DIMENSION, actualType);
    }

    @Test
    public void getTypeShouldReturnDateProviderForValidDatePatternAndScope() {
        ProviderType actualType = ProviderType.getType("YYYY", Scope.RENAME);
        assertEquals(ProviderType.DATE, actualType);
    }

    @Test
    public void getTypeShouldReturnNullForValidTokenAndInvalidScope() {
        ProviderType actualType = ProviderType.getType("SIZ", Scope.RENAME);
        assertNull(actualType);
    }

    @Test
    public void getTypeShouldReturnNullForInvalidToken() {
        ProviderType actualType = ProviderType.getType("Invalid Token", Scope.RENAME);
        assertNull(actualType);
    }

    @Test
    public void getTypeShouldReturnNullForValidDatePatternAndInvalidScope() {
        ProviderType actualType = ProviderType.getType("YYYY", Scope.FILTER);
        assertNull(actualType);
    }

    @Test
    public void getTypeShouldReturnNullForInvalidDatePatternAndValidScope() {
        ProviderType actualType = ProviderType.getType("Invalid Token", Scope.RENAME);
        assertNull(actualType);
    }

    @Test
    public void getTokenShouldReturnToken() {
        String token = ProviderType.FILE_NAME.getToken();
        assertEquals("FIN", token);
    }

    @Test
    public void getScopeShouldReturnArrayOfScope() {
        Scope[] scopes = ProviderType.FILE_NAME.getScope();
        assertArrayEquals(new Scope[]{Scope.RENAME, Scope.FILTER}, scopes);
    }

    @Test
    public void getAsProviderShouldReturnProvider() {
        ProviderType type = ProviderType.FILE_NAME;
        Provider<?> provider = type.getAsProvider(null);
        assertNotNull(provider);
        assertTrue(provider instanceof FileNameProvider);
    }

    @Test
    public void getAsOperandsShouldReturnPredicateOperands() throws ArgumentFormatException {
        ProviderType type = ProviderType.FILE_NAME;
        List<String> args = List.of("ABC", "DEF", "GHI");
        PredicateOperands<?> operands = type.getAsOperands(args);
        assertNotNull(operands);
        assertTrue(operands.operand() instanceof FileNameProvider);
        assertNotNull(operands.args());
        assertFalse(operands.args().isEmpty());
        assertTrue(operands.args().get(0) instanceof String);
    }

    @Test
    public void getAsOperandShouldThrowArgumentFormatExceptionIfGivenIncorrectArgumentList() {
        ProviderType type = ProviderType.DATE;
        List<String> args = List.of("Incorrect value");
        ArgumentFormatException exception = assertThrows(
                ArgumentFormatException.class,
                () -> type.getAsOperands(args),
                "Incorrect date format."
        );
        String actualToken = exception.getToken();
        String actualArgs = exception.getArg();
        assertEquals(type.getToken(), actualToken);
        assertEquals(args.get(0), actualArgs);
    }

    @Test
    public void getAsOperandsShouldThrowUnsupportedOperationExceptionIfProviderTypeDontSupportThisMethod() {
        ProviderType type = ProviderType.SEPARATOR;
        assertThrows(
                UnsupportedOperationException.class,
                () -> type.getAsOperands(null)
        );
    }
}