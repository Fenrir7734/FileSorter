package com.fenrir.filesorter.model.statement.types;

import com.fenrir.filesorter.model.enums.ArgumentNumber;
import com.fenrir.filesorter.model.enums.ReturnType;
import com.fenrir.filesorter.model.enums.Category;
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
    public void getTokenShouldReturnToken() {
        String token = ProviderType.FILE_NAME.getToken();
        assertEquals("FIN", token);
    }

    @Test
    public void getNameShouldReturnProviderName() {
        String name = ProviderType.DIRECTORY_NAME.getToken();
        assertEquals("DIN", name);
    }

    @Test
    public void getArgumentNumberShouldReturnProviderArgumentNumber() {
        ArgumentNumber argumentNumber = ProviderType.DATE_ACCESSED.getArgumentNumber();
        assertEquals(ArgumentNumber.SINGLE, argumentNumber);
    }

    @Test
    public void getScopeShouldReturnArrayOfScope() {
        Scope[] scopes = ProviderType.FILE_NAME.getScope();
        assertArrayEquals(new Scope[]{Scope.RENAME, Scope.FILTER}, scopes);
    }

    @Test
    public void getReturnTypeShouldReturnProviderReturnType() {
        ReturnType returnType = ProviderType.CATEGORY.getReturnType();
        assertEquals(ReturnType.EXACT_STRING, returnType);
    }

    @Test
    public void getGroupShouldReturnProviderGroup() {
        Category group = ProviderType.DIMENSION.getGroup();
        assertEquals(Category.PHOTO, group);
    }

    @Test
    public void getAsProviderShouldReturnProvider() throws ArgumentFormatException {
        ProviderType type = ProviderType.FILE_NAME;
        Provider<?> provider = type.getAsProvider(null);
        assertNotNull(provider);
        assertTrue(provider instanceof FileNameProvider);
    }

    @Test
    public void getAsProviderShouldReturnDateProviderForValidDatePattern() throws ArgumentFormatException {
        ProviderType type = ProviderType.DATE_CREATED;
        Provider<?> provider = type.getAsProvider(List.of("YYYY"));
        assertNotNull(provider);
        assertTrue(provider instanceof DateCreatedProvider);
    }

    @Test
    public void getAsProviderShouldThrowArgumentFormatExceptionForInvalidDatePattern() throws ArgumentFormatException {
        ProviderType type = ProviderType.DATE_CREATED;
        ArgumentFormatException exception = assertThrows(
                ArgumentFormatException.class,
                () -> type.getAsOperands(List.of("ABC")),
                "Invalid date format"
        );
        assertEquals("ABC", exception.getArg());
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
        ProviderType type = ProviderType.DATE_CREATED;
        List<String> args = List.of("Incorrect value");
        ArgumentFormatException exception = assertThrows(
                ArgumentFormatException.class,
                () -> type.getAsOperands(args),
                "Incorrect date format."
        );
        String actualArgs = exception.getArg();
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